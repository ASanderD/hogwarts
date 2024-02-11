package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final Map<Long, Student> students = new HashMap<>();
    private long studentsId = 0;

    public Student addStudent(Student student) {
        student.setId(++studentsId);
        students.put(studentsId, student);
        return student;
    }

    public Student findStudent(long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException();
        }
        return students.get(id);
    }

    public Student editStudent(long id, Student student) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException();
        }
        students.put(id, student);
        return student;
    }

    public Student deleteStudent(long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException();
        }
        return students.remove(id);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public List<Student> getStudentsByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
