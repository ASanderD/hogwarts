//package ru.hogwarts.school.service;
//
//import org.junit.jupiter.api.Test;
//import ru.hogwarts.school.exceptions.StudentNotFoundException;
//import ru.hogwarts.school.entity.Student;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
//
//class StudentServiceTest {
//
//    StudentService out = new StudentService();
//
//    @Test
//    void addStudentPositiveTest() {
//        Student expected = new Student(1L, "Harry Potter", 15);
//        assertThat(out.addStudent(new Student(0L, "Harry Potter", 15))).isEqualTo(expected);
//
//    }
//
//    @Test
//    void findStudentPositiveTest() {
//        Student expected = new Student(1L, "Harry Potter", 15);
//        out.addStudent(new Student(0L, "Harry Potter", 15));
//        assertThat(out.findStudent(1L)).isEqualTo(expected);
//    }
//
//    @Test
//    void findStudentNegativeTest() {
//        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> out.findStudent(1L));
//    }
//
//    @Test
//    void editStudentPositiveTest() {
//        Student expected = new Student(1L, "Harry Potter", 25);
//        out.addStudent(new Student(0L, "Harry Potter", 15));
//        assertThat(out.editStudent(1L, new Student(1L, "Harry Potter", 25))).isEqualTo(expected);
//    }
//
//    @Test
//    void editStudentNegativeTest() {
//        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> out.editStudent(1L, new Student(1L, "Harry Potter", 25)));
//    }
//
//    @Test
//    void deleteStudentPositiveTest() {
//        Student expected = new Student(1L, "Harry Potter", 15);
//        out.addStudent(new Student(0L, "Harry Potter", 15));
//        assertThat(out.findStudent(1L)).isEqualTo(expected);
//        out.deleteStudent(1L);
//        assertThat(out.getAllStudents()).doesNotContain(expected);
//    }
//
//    @Test
//    void deleteStudentNegativeTest() {
//        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> out.deleteStudent(1L));
//    }
//
//    @Test
//    void getAllStudentsPositiveTest() {
//        List<Student> expected = new ArrayList<>(List.of(
//                new Student(1L, "Harry Potter", 15),
//                new Student(2L, "Hermiona Graindger", 16),
//                new Student(3L, "Ron Wesley", 14)
//        ));
//        out.addStudent(new Student(0L, "Harry Potter", 15));
//        out.addStudent(new Student(2L, "Hermiona Graindger", 16));
//        out.addStudent(new Student(3L, "Ron Wesley", 14));
//        assertThat(out.getAllStudents()).containsAll(expected);
//    }
//
//    @Test
//    void getStudentsByAgePositiveTest() {
//        List<Student> expected = new ArrayList<>(List.of(
//                new Student(1L, "Harry Potter", 15),
//                new Student(3L, "Ron Wesley", 15)
//        ));
//        out.addStudent(new Student(0L, "Harry Potter", 15));
//        out.addStudent(new Student(2L, "Hermiona Graindger", 16));
//        out.addStudent(new Student(3L, "Ron Wesley", 15));
//        assertThat(out.getStudentsByAge(15)).containsAll(expected);
//
//    }
//}