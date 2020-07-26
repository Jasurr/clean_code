package com.example.lessons.demo.repository;

import com.example.lessons.demo.domain.FileStorage;
import com.example.lessons.demo.domain.FileStorageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {

    FileStorage findByHashId(String hashId);

    List<FileStorage> findAllByFileStorageStatus(FileStorageStatus status);
}
