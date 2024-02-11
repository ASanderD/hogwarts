package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long facultiesId = 0;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++facultiesId);
        faculties.put(facultiesId, faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException();
        }
        return faculties.get(id);
    }

    public Faculty editFaculty(long id,Faculty faculty) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException();
        }
        faculties.put(id, faculty);
        return faculty;
    }

    public Faculty deleteFaculty(long id) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException();
        }
        return faculties.remove(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    public List<Faculty> getFacultyByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}

