package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty facultyDb = facultyRepository.findById((student.getFaculty().getId()))
                    .orElseThrow(() -> new FacultyNotFoundException());
            student.setFaculty(facultyDb);
        }
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public Student editStudent(long id, Student student) {
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(student.getName());
                    oldStudent.setAge(student.getAge());
                    oldStudent.setFaculty(student.getFaculty());
                    return studentRepository.save(oldStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public Student deleteStudent(long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.deleteById(id);
                    return student;
                })
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student get(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty findFaculty(long id) {
        return get(id).getFaculty();
    }
}
