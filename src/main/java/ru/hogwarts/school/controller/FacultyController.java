package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public Faculty getStudentInfo(@PathVariable Long id) {
        return facultyService.findFaculty(id);
    }

    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @PostMapping
    public Faculty createStudent(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping("{id}")
    public Faculty editStudent(@PathVariable Long id, @RequestBody Faculty faculty) {
        return facultyService.editFaculty(id, faculty);
    }

    @DeleteMapping("{id}")
    public Faculty deleteStudent(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping ("/find-by-color")
    public List<Faculty> getFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }
}
