package com.example.demo.student;

import org.springframework.http.ResponseEntity;
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
    public List<Student> getAllStudents() {
        return STUDENTS;
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        return ResponseEntity.ok("Added student: " + student);
    }

    @DeleteMapping(path = "{id}")
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
