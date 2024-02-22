package com.perficient.udea.enrollment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.*;
import com.perficient.udea.enrollment.persistence.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    CoursePrerequisiteRepository coursePrerequisiteRepository;

    @Autowired
    CourseGradesRepository courseGradesRepository;

    @Autowired
    TermRepository termRepository;

    Syllabus computerScienceSyllabus;

    Student student;

    @BeforeEach
    public void setup() {

        termRepository.deleteAll();
        courseGradesRepository.deleteAll();
        classRoomRepository.deleteAll();
        courseRepository.deleteAll();

        computerScienceSyllabus = Syllabus.builder()
                .career("Computer Science Engineering")
                .totalCredits(210)
                .minimumTotalCredits(150)
                .build();

        student = Student.builder()
                .id("1152209135")
                .email("david.blj95@gmail.com")
                .syllabus(computerScienceSyllabus)
                .semester(2)
                .build();

        computerScienceSyllabus = syllabusRepository.save(computerScienceSyllabus);
        student = studentRepository.save(student);
    }

    @DisplayName("subscribeStudent - Should throw an exception when the student have an active enrollment")
    @Test
    public void shouldValidateActiveEnrollment() throws Exception {

        long invalidSession = System.currentTimeMillis() + 60000;
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().studentId(student.getId()).timestamp(invalidSession).classRoomIds(List.of(UUID.randomUUID().toString())).build();
        Term term = Term.builder().term("202401").active(true).students(Set.of(student)).build();
        termRepository.save(term);

        ResultActions response = mockMvc
                .perform(get("/api/v1/enrollment/" + student.getId() + "/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(equalTo("The student is already enrolled on the active term"))));
    }

    @DisplayName("getEnrollmentInformation - should filter completed courses from base offering")
    @Test
    @Transactional
    public void shouldExcludeCompletedCourses() throws Exception {

        int semester = 1;
        List<Course> courses = new ArrayList<>();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build();
        courses.add(differentialCalculus);
        courses.add(Course.builder().courseName("Mathematics").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courses.add(Course.builder().courseName("Algebra").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courseRepository.saveAll(courses);
        ClassRoom diffCalcMondayWednesday = ClassRoom.builder().course(differentialCalculus).schedule("10:00 - 12:00 AM").recurringDays("M-W").availableCapacity(40).build();
        classRoomRepository.save(diffCalcMondayWednesday);
        CourseGrades courseGrades = CourseGrades.builder().finalGrade(3.5).student(student).classRoom(diffCalcMondayWednesday).build();
        courseGradesRepository.save(courseGrades);

        ResultActions response = mockMvc
                .perform(get("/api/v1/enrollment/" + student.getId() + "/courses")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.courseDTOList.size()", is(courses.size() - 1)))
                .andExpect(jsonPath("$.courseDTOList[*].courseName", hasItems("Mathematics", "Algebra")));
    }

    @DisplayName("getEnrollmentInformation - should filter courses where one of its prerequisites is uncompleted")
    @Test
    @Transactional
    public void shouldExcludeCoursesWithUnmetPrerequisites() throws Exception {

        int semester = 1;
        List<Course> courses = new ArrayList<>();

        Course differentialCalculus = Course.builder().courseName("Differential Calculus").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build();
        Course integralCalculus = Course.builder().courseName("Integral Calculus").semester(semester + 1).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build();
        courses.add(differentialCalculus);
        courses.add(integralCalculus);
        courses.add(Course.builder().courseName("Mathematics").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courses.add(Course.builder().courseName("Algebra").semester(semester).syllabus(computerScienceSyllabus).id(UUID.randomUUID()).build());
        courseRepository.saveAll(courses);
        CoursePrerequisiteId idDiffCalcAndAlgebra = CoursePrerequisiteId.builder().course(integralCalculus).coursePrerequisite(differentialCalculus).build();
        CoursePrerequisite prerequisite = CoursePrerequisite.builder().coursePrerequisiteId(idDiffCalcAndAlgebra).build();
        coursePrerequisiteRepository.save(prerequisite);
        ClassRoom diffCalcMondayWednesday = ClassRoom.builder().course(differentialCalculus).schedule("10:00 - 12:00 AM").recurringDays("M-W").availableCapacity(40).build();
        classRoomRepository.save(diffCalcMondayWednesday);
        CourseGrades courseGrades = CourseGrades.builder().finalGrade(2.4).student(student).classRoom(diffCalcMondayWednesday).build();
        courseGradesRepository.save(courseGrades);

        ResultActions response = mockMvc.perform(get("/api/v1/enrollment/" + student.getId() + "/courses")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.courseDTOList.size()", is(courses.size())))
                .andExpect(jsonPath("$.courseDTOList[*].courseName", hasItems("Mathematics", "Algebra", "Differential Calculus")));
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

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.courseDTOList.size()", is(courses.size())))
                .andExpect(jsonPath("$.courseDTOList[*].courseName", hasItems("Differential Calculus", "Mathematics", "Algebra")));
    }

    @DisplayName("subscribeStudent - Should throw an exception when the student passed it's due time to subscribe")
    @Test
    public void shouldValidateSession() throws Exception {

        long invalidSession = System.currentTimeMillis() - 60000;
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().studentId(student.getId()).timestamp(invalidSession).classRoomIds(List.of(UUID.randomUUID().toString())).build();

        ResultActions response = mockMvc
                .perform(post("/api/v1/enrollment/" + student.getId() + "/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)));

        response.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", is(equalTo("The sessions is finished, the user took more than 15 minutes filling the form"))));
    }

    @DisplayName("subscribeStudent - Should throw an exception when the student have an active enrollment")
    @Test
    public void shouldValidateActiveEnrollmentOnSubscription() throws Exception {

        long invalidSession = System.currentTimeMillis() + 60000;
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().studentId(student.getId()).timestamp(invalidSession).classRoomIds(List.of(UUID.randomUUID().toString())).build();
        Term term = Term.builder().term("202401").active(true).students(Set.of(student)).build();
        termRepository.save(term);

        ResultActions response = mockMvc
                .perform(post("/api/v1/enrollment/" + student.getId() + "/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(equalTo("The student is already enrolled on the active term"))));
    }

    @DisplayName("subscribeStudent - Should throw an exception when non existing course ids were sent")
    @Test
    @Transactional
    public void shouldValidateInvalidClassroomIds() throws Exception {

        long invalidSession = System.currentTimeMillis() + 60000;
        List<String> classRooms = List.of(UUID.randomUUID().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().studentId(student.getId()).timestamp(invalidSession).classRoomIds(classRooms).build();

        ResultActions response = mockMvc
                .perform(post("/api/v1/enrollment/" + student.getId() + "/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(equalTo("The provided class rooms don't exist"))))
                .andExpect(jsonPath("$.errorList", hasItems(classRooms.getFirst())));
    }

    @DisplayName("subscribeStudent - Should throw an exception when a provided classroom is out of spots")
    @Test
    @Transactional
    public void shouldValidateClassRoomsOutOfSpots() throws Exception {

        long invalidSession = System.currentTimeMillis() + 60000;
        Course calculusCourse = Course.builder().courseName("Differential Calculus").syllabus(computerScienceSyllabus).semester(2).scienceField("Mathematics").build();
        calculusCourse = courseRepository.save(calculusCourse);
        ClassRoom diffCalcMondayWednesday = ClassRoom.builder().course(calculusCourse).originalCapacity(0).build();
        diffCalcMondayWednesday = classRoomRepository.save(diffCalcMondayWednesday);
        List<String> classRooms = List.of(diffCalcMondayWednesday.getId().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().studentId(student.getId()).timestamp(invalidSession).classRoomIds(classRooms).build();

        ResultActions response = mockMvc
                .perform(post("/api/v1/enrollment/" + student.getId() + "/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)));

        response.andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(equalTo("The provided classrooms don't have enough spots to subscribe new students. They were taken while the request was being processed or while the user was completing the form"))))
                .andExpect(jsonPath("$.errorList", hasItems(classRooms.getFirst())));
    }

    @DisplayName("subscribeStudent - Should throw an exception when a provided course id is not a valid course subscription")
    @Test
    @Transactional
    public void shouldValidateCourseSubscription() throws Exception {

        long invalidSession = System.currentTimeMillis() + 60000;
        // prerequisite pre-configuration
        Course differentialCalculus = Course.builder().courseName("differential Calculus").syllabus(computerScienceSyllabus).semester(1).scienceField("Mathematics").build();
        differentialCalculus = courseRepository.save(differentialCalculus);
        CoursePrerequisiteId idDiffCalcAndAlgebra = CoursePrerequisiteId.builder().coursePrerequisite(differentialCalculus).build();
        CoursePrerequisite prerequisite = CoursePrerequisite.builder().coursePrerequisiteId(idDiffCalcAndAlgebra).build();
        HashSet<CoursePrerequisite> coursePrerequisites = new HashSet<>();
        coursePrerequisites.add(prerequisite);
        // target course configuration with its corresponding prerequisite
        Course integralCalculus = Course.builder().courseName("Integral Calculus").syllabus(computerScienceSyllabus).semester(2).scienceField("Mathematics").coursePrerequisites(coursePrerequisites).build();
        integralCalculus = courseRepository.save(integralCalculus);
        courseRepository.save(integralCalculus);
        // classrooms to persists
        ClassRoom integralCalcMondayWednesday = ClassRoom.builder().course(integralCalculus).originalCapacity(40).schedule("10:00 - 12:00 AM").recurringDays("M-W").build();
        ClassRoom differentialCalcMondayWednesday = ClassRoom.builder().course(differentialCalculus).originalCapacity(40).schedule("10:00 - 12:00 AM").recurringDays("M-W").build();
        integralCalcMondayWednesday = classRoomRepository.save(integralCalcMondayWednesday);
        differentialCalcMondayWednesday = classRoomRepository.save(differentialCalcMondayWednesday);
        // classroom configuration with failing grades
        CourseGrades courseGrades = CourseGrades.builder().finalGrade(2.5).student(student).classRoom(differentialCalcMondayWednesday).build();
        courseGradesRepository.save(courseGrades);
        List<String> classRooms = List.of(integralCalcMondayWednesday.getId().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder()
                .studentId(student.getId())
                .timestamp(invalidSession)
                .classRoomIds(classRooms).build();

        ResultActions response = mockMvc
                .perform(post("/api/v1/enrollment/" + student.getId() + "/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(equalTo("The provided course class rooms are not yet unlocked because the user either has not met the requirements or they are already completed"))))
                .andExpect(jsonPath("$.errorList", hasItems(classRooms.getFirst())));
    }

    @DisplayName("subscribeStudent - Should post a subscription successfully")
    @Test
    @Transactional
    public void shouldPostASuccessfulInscription() throws Exception {

        long invalidSession = System.currentTimeMillis() + 60000;
        Course calculusCourse = Course.builder().courseName("Differential Calculus").syllabus(computerScienceSyllabus).semester(2).scienceField("Mathematics").build();
        calculusCourse = courseRepository.save(calculusCourse);
        ClassRoom diffCalcMondayWednesday = ClassRoom.builder().course(calculusCourse).originalCapacity(40).build();
        diffCalcMondayWednesday = classRoomRepository.save(diffCalcMondayWednesday);
        List<String> classRooms = List.of(diffCalcMondayWednesday.getId().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().studentId(student.getId()).timestamp(invalidSession).classRoomIds(classRooms).build();

        ResultActions response = mockMvc
                .perform(post("/api/v1/enrollment/" + student.getId() + "/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)));

        response.andExpect(status().isNoContent());
    }
}