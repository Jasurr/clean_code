package com.example.lessons.demo.web.rest;

import com.example.lessons.demo.Security.SecurityUtils;
import com.example.lessons.demo.domain.Employee;
import com.example.lessons.demo.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmployeeReource {
    private final EmployeeService employeeService;

    public EmployeeReource(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees")
    public ResponseEntity create(@RequestBody Employee employee) {
        Employee employee1 = employeeService.save(employee);
        return ResponseEntity.ok(employee1);
    }

    @PutMapping("/employees")
    public ResponseEntity update(@RequestBody Employee employee) {
        Employee employee1 = employeeService.save(employee);
        return ResponseEntity.ok(employee1);
    }

    @GetMapping("/employees")
    public ResponseEntity getAll() {
        List<Employee> employeeList = employeeService.listAll();
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/employees/{name}")
    public ResponseEntity getAll(@PathVariable String name) {
        Optional<String> optional = SecurityUtils.getCurrentUserName();
        List<Employee> employeeList = employeeService.findByName(name);
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/employees/search")
    public ResponseEntity getAllSearch(@RequestParam String name) {
        List<Employee> employeeList = employeeService.findAllByParam(name);
        return ResponseEntity.ok(employeeList);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok("Qator O'chirildi");
    }

}
