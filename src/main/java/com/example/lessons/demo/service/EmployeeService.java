package com.example.lessons.demo.service;

import com.example.lessons.demo.domain.Employee;
import com.example.lessons.demo.repository.EmployeeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).get();
    }

    public List<Employee> findByName(String name) {
        return employeeRepository.findByNameNativeQuery(name);
    }

    public List<Employee> findAllByParam(String name) {
        return employeeRepository.findAllByLike(name);
    }

    public void delete(Long id) {
        Employee employee = employeeRepository.getOne(id);
        employeeRepository.delete(employee);
    }

    @Scheduled(cron = "0 32 23 * * *")
    public Employee saveSchedule() {
        Employee employee = new Employee();
        employee.setName("Mirzoxid");
        employee.setLastName("Ulug'murodov");
        return employeeRepository.save(employee);
    }

}
