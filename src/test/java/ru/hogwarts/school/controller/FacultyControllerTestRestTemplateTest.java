package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplateTest {


    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyController facultyController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;
    private Faculty griffindor;
    private Faculty slizerin;
    private List<Faculty> faculties;

    @Test
    void contextLoads() {
        assertThat(facultyController).isNotNull();
    }

    @AfterEach
    public void afterEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        griffindor = new Faculty();
        slizerin = new Faculty();
        griffindor.setName("Гриффиндор");
        griffindor.setColor("Жёлтый");
        slizerin.setName("Слизерин");
        slizerin.setColor("Зелёный");
        faculties = facultyRepository.saveAll(List.of(griffindor, slizerin));
    }

    @Test
    void getFaculty() {
        long id = griffindor.getId();
        final ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id, Faculty.class);
        Faculty facultyGetBody = response.getBody();
        Optional<Faculty> fromDb = facultyRepository.findById(id);
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get()).isEqualTo(facultyGetBody);
    }

    @Test
    void getFacultyInfoNegativeTest() {
        Optional<Faculty> faculty = facultyRepository.findById(slizerin.getId());
        long id = faculty.get().getId();
        final ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id + 1, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllFaculties() {
        final ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<Faculty> findFaculties = response.getBody();
        assertThat(findFaculties).isEqualTo(faculties);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(findFaculties).isNotNull();
    }

    @Test
    void createFaculty() {
        Faculty ravenclaw = new Faculty();
        ravenclaw.setName("Когтевран");
        ravenclaw.setColor("Пурпурный");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                ravenclaw,
                Faculty.class);
        Faculty created = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(ravenclaw);
        Optional<Faculty> fromDb = facultyRepository.findById(created.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .isEqualTo(created);
    }

    @Test
    void updatePositiveTest() {
        long id = slizerin.getId();
        Faculty change = new Faculty();
        change.setName("Когтевран");
        change.setColor("Красный");
        final ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(change),
                Faculty.class);
        Faculty actual = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(change)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actual);

        Optional<Faculty> fromDb = facultyRepository.findById(actual.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .isEqualTo(actual);
    }

    @Test
    void updateNegativeTest() {
        long id = griffindor.getId();
        Faculty change = new Faculty();
        change.setName("Когтевран");
        change.setColor("Красный");
        final ResponseEntity<Faculty> response = restTemplate.exchange(
                String.format("http://localhost:" + port + "/faculty/" + id + 1),
                HttpMethod.PUT,
                new HttpEntity<>(change),
                Faculty.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete() {
        long id = griffindor.getId();
        restTemplate.delete("http://localhost:" + port + "/faculty/{id}", id, String.class);
        final ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getFacultyByColor() {
        String color = "Жёлтый";
        List<Faculty> expected = faculties.stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();
        final ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?color=" + color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                Map.of("color", color)
        );
        List<Faculty> actual = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
        assertThat(actual).isNotNull();
    }

    @Test
    void findByNameOrColor() {
        String color = "Жёлтый";
        String name = "Слизерин";

        List<Faculty> expectedByColor = faculties.stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();
        List<Faculty> expectedByName = faculties.stream()
                .filter(faculty -> faculty.getName().equals(name))
                .toList();
        final ResponseEntity<List<Faculty>> responseByColor = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?nameOrColor=" + color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                Map.of("color", color)
        );

        final ResponseEntity<List<Faculty>> responseByName = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?nameOrColor=" + name,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                Map.of("name", name)
        );
        List<Faculty> actualByColor = responseByColor.getBody();
        assertThat(responseByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualByColor).isEqualTo(expectedByColor);
        assertThat(actualByColor).isNotNull();

        List<Faculty> actualByName = responseByName.getBody();
        assertThat(responseByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualByName).isEqualTo(expectedByName);
        assertThat(actualByName).isNotNull();
    }

    @Test
    void findStudentsOfFaculty() {
        long id = griffindor.getId();
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

        List<Student> students = studentRepository.saveAll(List.of(harryPotter, hermioneGranger, drakoMalfoy));

        List<Student> expected = students.stream()
                .filter(student -> student.getFaculty().getId() == id)
                .toList();

        final ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + id + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<Student> actual = response.getBody();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}