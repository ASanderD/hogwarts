package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(studentController).isNotNull();
    }

    @Test
    void setUp() {
        Faculty griffindor = new Faculty();
        Faculty slizerin = new Faculty();
        griffindor.setId(1L);
        slizerin.setId(2L);

        Student harryPotter = new Student();
        harryPotter.setName("Гарри Поттер");
        harryPotter.setAge(25);
        harryPotter.setFaculty(griffindor);

        Student hermioneGranger = new Student();
        hermioneGranger.setName("Гермиона Грейнджер");
        hermioneGranger.setAge(23);
        hermioneGranger.setFaculty(griffindor);

        Student drakoMalfoy = new Student();
        drakoMalfoy.setName("Драко Малфой");
        drakoMalfoy.setAge(18);
        drakoMalfoy.setFaculty(slizerin);

        restTemplate.postForObject("http://localhost:" + port + "/student", harryPotter, String.class);
        restTemplate.postForObject("http://localhost:" + port + "/student", hermioneGranger, String.class);
        restTemplate.postForObject("http://localhost:" + port + "/student", drakoMalfoy, String.class);
    }

    @Test
    void getStudentInfoPositiveTest() {
        long id = 1L;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student" + id, String.class)).isNotNull();
    }

    @Test
    void getStudentInfoNegativeTest() {
        long id = 100L;
        final ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/student/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllStudents() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class)).isNotNull();
    }

    @Test
    void findByAgeBetween() {
        int min = 20;
        int max = 25;
        Student student = new Student();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/student?min=" + min + "&max=" + max)
                .queryParam("min", min)
                .queryParam("max", max);

        final ResponseEntity<String> response = restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(student),
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    @Test
    void createStudent() {
        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        student.setName("Рон Уизли");
        student.setAge(24);
        student.setFaculty(faculty);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class)).isNotNull();
    }

    @Test
    void editStudentPositiveTest() {
        long id = 1L;
        Student change = new Student();
        Faculty slizerin = new Faculty();
        slizerin.setId(2L);
        change.setName("Рон Уизли");
        change.setAge(24);
        change.setFaculty(slizerin);
        final ResponseEntity<Student> response = restTemplate.exchange(
                String.format("http://localhost:" + port + "/student/" + id),
                HttpMethod.PUT,
                new HttpEntity<>(change),
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void editStudentNegativeTest() {
        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(2L);
        long id = 100L;
        student.setName("Рон Уизли");
        student.setAge(24);
        student.setFaculty(faculty);
        final ResponseEntity<Student> response = restTemplate.exchange(
                String.format("http://localhost:" + port + "/student/" + id),
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteStudent() {
        long id = 2L;
        restTemplate.delete("http://localhost:" + port + "/student/" + id, String.class);
        final ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/student/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getStudentsByAge() {
        int age = 23;
        Student student = new Student();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/student?age=" + age)
                .queryParam("age", age);

        final ResponseEntity<String> response = restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(student),
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);
    }

    @Test
    void findFaculty() {
        long id = 3L;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id + "/faculty", String.class)).isNotNull();
        final ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/student/" + id + "/faculty", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);

    }
}