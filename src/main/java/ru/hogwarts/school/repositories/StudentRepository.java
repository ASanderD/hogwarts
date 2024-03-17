package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
//    Student findById(long id);
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int min, int max);
    List<Student> findByFaculty_Id(long id);

    @Query(value = "SELECT COUNT(*) FROM students",nativeQuery = true)
    Integer getNumberOfStudents();

    @Query(value = "SELECT avg(age) FROM students",nativeQuery = true)
    Float getAverageAgeOfStudents();

    @Query(value = "SELECT * FROM (SELECT * FROM students ORDER BY id DESC LIMIT 5) ORDER BY id",nativeQuery = true)
    List<Student> getLastFiveStudents();

}
