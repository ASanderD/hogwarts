package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FacultyServiceTest {

    private final FacultyService out = new FacultyService();

    @Test
    void addFacultyPositiveTest() {
        Faculty expected = new Faculty(1L, "Griffindor", "Yellow");
        assertThat(out.addFaculty(new Faculty(0L, "Griffindor", "Yellow"))).isEqualTo(expected);

    }

    @Test
    void findFacultyPositiveTest() {
        Faculty expected = new Faculty(1L, "Griffindor", "Yellow");
        out.addFaculty(new Faculty(0L, "Griffindor", "Yellow"));
        assertThat(out.findFaculty(1L)).isEqualTo(expected);
    }

    @Test
    void findFacultyNegativeTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> out.findFaculty(1L));
    }

    @Test
    void editFacultyPositiveTest() {
        Faculty expected = new Faculty(1L, "Griffindor", "Yellow");
        out.addFaculty(new Faculty(0L, "Griffindor", "Yellow"));
        assertThat(out.editFaculty(1L, new Faculty(1L, "Griffindor", "Yellow"))).isEqualTo(expected);
    }

    @Test
    void editFacultyNegativeTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> out.editFaculty(1L, new Faculty(1L, "Griffindor", "Yellow")));
    }

    @Test
    void deleteFacultyPositiveTest() {
        Faculty expected = new Faculty(1L, "Griffindor", "Yellow");
        out.addFaculty(new Faculty(0L, "Griffindor", "Yellow"));
        assertThat(out.findFaculty(1L)).isEqualTo(expected);
        out.deleteFaculty(1L);
        assertThat(out.getAllFaculties()).doesNotContain(expected);
    }

    @Test
    void deleteFacultyNegativeTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> out.deleteFaculty(1L));
    }

    @Test
    void getAllFacultiesPositiveTest() {
        List<Faculty> expected = new ArrayList<>(List.of(
                new Faculty(1L, "Griffindor", "Yellow"),
                new Faculty(2L, "Slizering", "Green"),
                new Faculty(3L, "Puffenduy", "Blue")
        ));
        out.addFaculty(new Faculty(1L, "Griffindor", "Yellow"));
        out.addFaculty(new Faculty(2L, "Slizering", "Green"));
        out.addFaculty(new Faculty(3L, "Puffenduy", "Blue"));
        assertThat(out.getAllFaculties()).containsAll(expected);
    }

    @Test
    void getFacultyByColorPositiveTest() {
        List<Faculty> expected = new ArrayList<>(List.of(
                new Faculty(1L, "Griffindor", "Yellow")
        ));
        out.addFaculty(new Faculty(1L, "Griffindor", "Yellow"));
        out.addFaculty(new Faculty(2L, "Slizering", "Green"));
        out.addFaculty(new Faculty(3L, "Puffenduy", "Blue"));
        assertThat(out.getFacultyByColor("Yellow")).containsAll(expected);
    }
}