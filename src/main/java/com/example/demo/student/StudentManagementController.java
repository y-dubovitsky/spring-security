package com.example.demo.student;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/management/api/v1/studens")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<Student> getAllStudents() {
        return STUDENTS;
    }

    @GetMapping("/tr")
    @PreAuthorize("hasRole('ROLE_TRAINEE_ADMIN')")
    public ResponseEntity<String> getOne() {
        return ResponseEntity.ok("TRAINEE_ADMIN");
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('student:write')")
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        return ResponseEntity.ok("Added student: " + student);
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAnyAuthority('student:read', 'student:write', 'course:read', 'course:write')")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") Integer id) {
        return ResponseEntity.ok("Student deleted: " + STUDENTS.stream()
                .filter(student -> student.getStudentId() == id)
                .findFirst()
                .orElseThrow()
        );
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Integer id) {
        return ResponseEntity.ok("User updated with id " + id);
    }

}
