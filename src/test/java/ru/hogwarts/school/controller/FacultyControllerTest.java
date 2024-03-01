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
class FacultyControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyController facultyController;

    @Test
    void contextLoads() {
        assertThat(facultyController).isNotNull();
    }

    @Test
    void getFaculty() {
        long id = 1L;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty" + id, String.class)).isNotNull();
    }

    @Test
    void getAllFaculties() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class)).isNotNull();
    }

    @Test
    void createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(4L);
        faculty.setName("Когтевран");
        faculty.setColor("Пурпурный");
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class)).isNotNull();
    }

    @Test
    void updatePositiveTest() {
        long id = 5L;
        Faculty faculty = new Faculty();
        faculty.setName("Когтевран");
        faculty.setColor("Красный");
        final ResponseEntity<Faculty> response = restTemplate.exchange(
                String.format("http://localhost:" + port + "/faculty/" + id),
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }   @Test
    void updateNegativeTest() {
        long id = 10L;
        Faculty faculty = new Faculty();
        faculty.setName("Когтевран");
        faculty.setColor("Красный");
        final ResponseEntity<Faculty> response = restTemplate.exchange(
                String.format("http://localhost:" + port + "/faculty/" + id),
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete() {
        long id = 4L;
        restTemplate.delete("http://localhost:" + port + "/faculty/" + id, String.class);
        final ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getFacultyByColor() {
        Faculty faculty = new Faculty();
        UriComponentsBuilder builder= UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/faculty")
                .queryParam("color","Жёлтый");
        final ResponseEntity<String> response = restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(faculty),
                String.class);
//        final ResponseEntity<String> response = restTemplate.getForEntity(
//                builder.build().toUriString(),String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    @Test
    void findByNameOrColor() {
        String color = "Жёлтый";
        String name = "Слизерин";

        Faculty faculty = new Faculty();
        UriComponentsBuilder builder= UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/faculty")
                .queryParam("nameOrColor",color);

        final ResponseEntity<String> response = restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(faculty),
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);
    }

    @Test
    void findStudentsOfFaculty() {
        long id = 1L;
        final ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id + "/students", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);
    }
}