package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.debug("Faculty {} create", faculty.getName());
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Faculty update(long id, Faculty faculty) {
        logger.debug("Faculty updated with parameters id = {}, facultyName = {}", id, faculty.getName());
        return facultyRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setName(faculty.getName());
                    oldFaculty.setColor(faculty.getColor());
                    return facultyRepository.save(oldFaculty);
                })
                .orElseThrow(() -> new FacultyNotFoundException());
    }

    public Faculty delete(long id) {
        logger.debug("Faculty № {} delete", id);
        return facultyRepository.findById(id)
                .map(faculty -> {
                    facultyRepository.deleteById(id);
                    return faculty;
                })
                .orElseThrow(() -> new FacultyNotFoundException());
    }

    public Collection<Faculty> getAllFaculties() {
        logger.debug("Method getAllFaculties invoked");
        return facultyRepository.findAll();
    }

    public Faculty get(long id) {
        logger.debug("Get Faculty by id = {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException());
    }

    public List<Faculty> getFacultyByColor(String color) {
        logger.debug("Method getFacultyByColor invoked");
        return facultyRepository.findAllByColor(color);
    }

    public List<Faculty> findByNameOrColor(String nameOrColor) {
        logger.debug("Method findByNameOrColor invoked");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    public List<Student> findStudentsByFacultyId(long id) {
        logger.debug("Method findStudentsByFacultyId invoked");
        Faculty faculty = get(id);
        return studentRepository.findByFaculty_Id(faculty.getId());
    }

    public String longestNameOfFaculty() {
        return facultyRepository.findAll().stream()
                .max(Comparator.comparing(faculty -> faculty.getName().length()))
                .map(faculty -> faculty.getName())
                .orElseThrow();
    }
}

