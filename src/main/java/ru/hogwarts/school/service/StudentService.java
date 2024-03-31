package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student addStudent(Student student) {
        logger.debug("Student {} create", student.getName());
        student.setId(null);
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty facultyDb = facultyRepository.findById((student.getFaculty().getId()))
                    .orElseThrow(() -> new FacultyNotFoundException());
            student.setFaculty(facultyDb);
        }
        return studentRepository.save(student);
    }

    public Student editStudent(Long id, Student student) {
        logger.debug("Student updated with parameters id = {}, studentName = {}", id, student.getName());
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(student.getName());
                    oldStudent.setAge(student.getAge());
                    oldStudent.setFaculty(student.getFaculty());
                    return studentRepository.save(oldStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public Student deleteStudent(Long id) {
        logger.debug("Student № {} delete", id);
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.deleteById(id);
                    return student;
                })
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public Collection<Student> getAllStudents() {
        logger.debug("Method get all Students invoked");
        return studentRepository.findAll();
    }

    public Student get(Long id) {
        logger.debug("Get Students by id = {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException());
    }


    public List<Student> getStudentsByAge(int age) {
        logger.debug("Get Students by age = {}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.debug("Get Students with age between minAge = {}, maxAge = {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty findFaculty(long id) {
        logger.debug("Get Faculty of Student with id = {}", id);
        return get(id).getFaculty();
    }

    public Integer getNumberOfStudents() {
        logger.debug("Get number of Students at school successful");
        return studentRepository.getNumberOfStudents();
    }

    public Float getAverageAgeOfStudents() {
        logger.debug("Get get average age of Students at school successful");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<Student> getLastFiveStudents() {
        logger.debug("Get get last five Students successful");
        return studentRepository.getLastFiveStudents();
    }

    public List<String> namesStartsWithG() {
        logger.debug("Get names Students starts with G  successful");
        return studentRepository.findAll().stream()
                .filter(student -> student.getName().toUpperCase().startsWith("Г"))
                .map(student -> student.getName().toUpperCase())
                .sorted()
                .toList();
    }

    public Double getAverageAgeOfStudentsWithStream() {
        logger.debug("Get average age of Students with stream successful");
        return studentRepository.findAll().stream()
                .mapToDouble(student -> student.getAge())
                .average()
                .orElseThrow();
    }

    public void studentsPrintParallel() {
        List<String> studentsNames = studentRepository.findAll().stream()
                .map(Student::getName)
                .limit(6)
                .toList();
        logger.info(studentsNames.get(0));
        logger.info(studentsNames.get(1));
        new Thread(() -> {
            logger.info(studentsNames.get(2));
            logger.info(studentsNames.get(3));
        }).start();
        new Thread(() -> {
            logger.info(studentsNames.get(4));
            logger.info(studentsNames.get(5));
        }).start();
    }

    public void studentsPrintSynchronized() {
        List<Student> students = studentRepository.findAll().stream()
                .limit(6)
                .toList();
        print(students.get(0), students.get(1));
        new Thread(() -> {
            print(students.get(2), students.get(3));
        }).start();
        new Thread(() -> {
            print(students.get(4), students.get(5));
        }).start();
    }

    private synchronized void print(Student... students) {
        Arrays.stream(students).forEach(student -> logger.info(student.getName()));
    }
}
