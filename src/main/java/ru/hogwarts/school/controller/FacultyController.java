package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
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
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.get(id);
    }

    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping("{id}")
    public Faculty update(@PathVariable Long id, @RequestBody Faculty faculty) {
        return facultyService.update(id, faculty);
    }

    @DeleteMapping("{id}")
    public Faculty delete(@PathVariable Long id) {
        return facultyService.delete(id);
    }

    @GetMapping(params = "color")
    public List<Faculty> getFacultyByColor(@RequestParam(required = false) String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping(params = "nameOrColor")
    public List<Faculty> findByNameOrColor(@RequestParam(required = false) String nameOrColor) {
        return facultyService.findByNameOrColor(nameOrColor);
    }

    @GetMapping("/{id}/students")
    public List<Student> findStudentsOfFaculty(@PathVariable Long id) {
        return facultyService.findStudentsByFacultyId(id);
    }

    @GetMapping("longestNameOfFaculty")
    public String longestNameOfFaculty() {
        return facultyService.longestNameOfFaculty();
    }


}
