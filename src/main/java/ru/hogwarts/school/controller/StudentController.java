package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public Student getStudentInfo(@PathVariable Long id) {
        return studentService.findStudent(id);
    }

    @Operation(summary = "Получить всех студентов")
    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    @Operation(summary = "Получить студентов, входящих в определенный интервал по возрасту")
    @GetMapping(params = {"min", "max"})
    public Collection<Student> findByAgeBetween(@RequestParam  (required = false) int min, @RequestParam (required = false) int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("{id}")
    public Student editStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.editStudent(id, student);
    }

    @DeleteMapping("{id}")
    public Student deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }
    @Operation(summary = "Получить студентов определенного возраста")
    @GetMapping(params = "age")
    public List<Student> getStudentsByAge(@RequestParam(required = false) int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("{id}/faculty")
    public Faculty findFaculty(@PathVariable Long id) {
        return studentService.findFaculty(id);
    }
}
