package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int min, int max);

    List<Student> findByFaculty_Id(long id);
}