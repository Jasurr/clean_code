package com.example.lessons.demo.service;

import com.example.lessons.demo.domain.FileStorage;
import com.example.lessons.demo.domain.FileStorageStatus;
import com.example.lessons.demo.repository.FileStorageRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class FileStorageService {

    private final FileStorageRepository fileStorageRepository;
    private final Hashids hashids;
    @Value("${upload.folder}")
    private String uploadFolder;

    public FileStorageService(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.hashids = new Hashids(getClass().getName(), 6);
    }

    public void save(MultipartFile file) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setName(file.getOriginalFilename());
        fileStorage.setExtension(getExt(file.getOriginalFilename()));
        fileStorage.setFileSize(file.getSize());
        fileStorage.setContentType(file.getContentType());
        fileStorage.setFileStorageStatus(FileStorageStatus.DRAFT);

        fileStorageRepository.save(fileStorage);

        Date date = new Date();
        File uploadFolder = new File(String.format("%s/upload_files/%d/%d/%d/", this.uploadFolder,
                1900 + date.getYear(), 1 + date.getMonth(), date.getDate()));
        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
            System.out.println("aytilgan papkalar yaratildi");
        }
        fileStorage.setHashId(hashids.encode(fileStorage.getId()));
        fileStorage.setUploadPath(String.format("upload_files/%d/%d/%d/%s.%s", 1900 + date.getYear(), 1 + date.getMonth(), date.getDate(),
                fileStorage.getHashId(),
                fileStorage.getExtension()));
        fileStorageRepository.save(fileStorage);

        uploadFolder = uploadFolder.getAbsoluteFile();
        File file1 = new File(uploadFolder, String.format("%s.%s", fileStorage.getHashId(), fileStorage.getExtension()));

        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getExt(String fileName) {
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf(".");
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext = fileName.substring((dot + 1));
            }
        }
        return ext;
    }

    @Transactional(readOnly = true)
    public FileStorage findByHashId(String hashId) {
        return fileStorageRepository.findByHashId(hashId);
    }

    public void delete(String hashId) {
        FileStorage fileStorage = fileStorageRepository.findByHashId(hashId);
        File file = new File(String.format("%s/%s", this.uploadFolder, fileStorage.getUploadPath()));

        if (file.delete()) {
            fileStorageRepository.delete(fileStorage);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAll() {
        List<FileStorage> fileStorages = fileStorageRepository.findAllByFileStorageStatus(FileStorageStatus.DRAFT);
        fileStorages.forEach(fileStorage -> {
            delete(fileStorage.getHashId());
        });
    }

}
