package com.perficient.udea.enrollment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perficient.udea.enrollment.entities.*;
import com.perficient.udea.enrollment.repositories.*;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    CourseGradesRepository courseGradesRepository;

    Syllabus computerScienceSyllabus;

    Student student;

    @BeforeEach
    public void setup() {

        computerScienceSyllabus = Syllabus.builder()
                .career("Computer Science Engineering")
                .totalCredits(210)
                .minimumTotalCredits(150)
                .build();

        student = Student.builder()
                .id("1152209135")
                .email("david.blj95@gmail.com")
                .syllabus(computerScienceSyllabus)
                .semester(1)
                .build();

        computerScienceSyllabus = syllabusRepository.save(computerScienceSyllabus);
        student = studentRepository.save(student);
        courseRepository.deleteAll();
    }

    @DisplayName("getEnrollmentInformation - should filter completed courses from base offering")
    @Test
    @Transactional
    public void shouldFilterCompletedCoursesFromBaseOffering() throws Exception {

        int semester = 1;
        List<Course> courses = new ArrayList<>();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build();
        courses.add(differentialCalculus);
        courses.add(Course.builder().courseName("Mathematics").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courses.add(Course.builder().courseName("Algebra").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courseRepository.saveAll(courses);
        ClassRoom diffCalcMondayWednesday = ClassRoom.builder()
                .course(differentialCalculus)
                .schedule("10:00 - 12:00 AM")
                .recurringDays("M-W")
                .availableCapacity(40)
                .build();
        classRoomRepository.save(diffCalcMondayWednesday);
        CourseGrades courseGrades = CourseGrades.builder()
                .finalGrade(3.5)
                .student(student)
                .classRoom(diffCalcMondayWednesday)
                .build();
        courseGradesRepository.save(courseGrades);

        ResultActions response = mockMvc
                .perform(get("/api/v1/enrollment/" + student.getId() + "/courses")
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseDTOList.size()", is(courses.size() - 1)))
                .andExpect(jsonPath("$.courseDTOList[*].courseName", hasItem("Mathematics")))
                .andExpect(jsonPath("$.courseDTOList[*].courseName", hasItem("Algebra")));
    }

    @DisplayName("getEnrollmentInformation - should filter courses where one of its prerequisites is uncompleted")
    @Test
    @Transactional
    public void shouldNotOfferUncompletedPrerequisites() throws Exception {

        int semester = 1;
        List<Course> courses = new ArrayList<>();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build();
        courses.add(differentialCalculus);
        courses.add(Course.builder().courseName("Mathematics").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courses.add(Course.builder().courseName("Algebra").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courseRepository.saveAll(courses);
        ClassRoom diffCalcMondayWednesday = ClassRoom.builder()
                .course(differentialCalculus)
                .schedule("10:00 - 12:00 AM")
                .recurringDays("M-W")
                .availableCapacity(40)
                .build();
        classRoomRepository.save(diffCalcMondayWednesday);
        CourseGrades courseGrades = CourseGrades.builder()
                .finalGrade(3.5)
                .student(student)
                .classRoom(diffCalcMondayWednesday)
                .build();
        courseGradesRepository.save(courseGrades);

        ResultActions response = mockMvc.perform(get("/api/v1/enrollment/" + student.getId() + "/courses")
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseDTOList.size()", is(courses.size())));
    }

    @DisplayName("getEnrollmentInformation - should list all available courses")
    @Test
    @Transactional
    public void shouldListAvailableCoursesToEnroll() throws Exception {

        int semester = 1;
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder().courseName("Differential Calculus").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courses.add(Course.builder().courseName("Mathematics").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courses.add(Course.builder().courseName("Algebra").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courseRepository.saveAll(courses);

        ResultActions response = mockMvc.perform(get("/api/v1/enrollment/" + student.getId() + "/courses")
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseDTOList.size()", is(courses.size())));
    }
}