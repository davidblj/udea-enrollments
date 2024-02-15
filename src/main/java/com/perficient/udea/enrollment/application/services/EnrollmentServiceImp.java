package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.application.DTOs.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.*;
import com.perficient.udea.enrollment.persistence.repositories.*;
import com.perficient.udea.enrollment.presentation.errors.exceptions.EnrollmentInvalidSessionException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.InvalidCourseEnrollmentException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.InvalidCourseTrayException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.NoSpotsAvailableException;
import com.perficient.udea.enrollment.application.mappers.SimpleCourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImp implements EnrollmentService {

    private final CourseGradesRepository courseGradesRepository;

    private final ClassRoomRepository classRoomRepository;

    private final TermRepository termRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final SimpleCourseMapper simpleCourseMapper;

    @Override
    public void subscribeStudent(SubscriptionDTO subscriptionDTO) {
        List<UUID> classRoomIds = subscriptionDTO.getClassRoomIds().stream().map(UUID::fromString).toList();
        List<Course> availableCourses = getAvailableCourses(subscriptionDTO.getStudentId());
        List<ClassRoom> classRoomsToEnroll = classRoomRepository.getAllByIdIn(classRoomIds);
        validateSubscription(classRoomsToEnroll, subscriptionDTO, availableCourses);
        classRoomRepository.saveAll(classRoomsToEnroll.stream().map(getUpdatedClassRoom(subscriptionDTO)).toList());
        courseGradesRepository.saveAll(classRoomsToEnroll.stream().map(getDefaultGrades(subscriptionDTO)).toList());
        createTermEnrollment(subscriptionDTO);
    }

    private void validateSubscription(List<ClassRoom> classRooms, SubscriptionDTO subscriptionDTO, List<Course> availableCourses) {
        validateSession(subscriptionDTO.getTimestamp());
        validateActiveEnrollment(subscriptionDTO.getStudentId());
        validateInvalidCourseIds(classRooms, subscriptionDTO);
        validateClassroomsWithoutSpots(classRooms);
        validateInvalidCourseSubscriptions(classRooms, availableCourses);
    }

    private void validateSession(Long session) {
        if (System.currentTimeMillis() > session) {
            throw new EnrollmentInvalidSessionException("The sessions is finished, the user took more than 15 minutes filling the form");
        }
    }

    private void validateInvalidCourseSubscriptions(List<ClassRoom> classRooms, List<Course> availableCourses) {
        List<ClassRoom> invalidEnrollments = classRooms.stream()
                .filter(classRoom -> availableCourses.stream().noneMatch(course -> course.getId().equals(classRoom.getCourse().getId())))
                .toList();
        if (!invalidEnrollments.isEmpty()) {
            List<String> invalidClassRoomsIds = invalidEnrollments.stream().map(classRoom -> classRoom.getId().toString()).toList();
            throw new InvalidCourseTrayException(invalidClassRoomsIds, "The provided course class rooms are not yet unlocked because the user either has not " +
                    "met the requirements or they are already completed");
        }
    }

    private void validateClassroomsWithoutSpots(List<ClassRoom> classRooms) {
        boolean classRoomsOutOfSpots = classRooms.stream().anyMatch(classRoom -> classRoom.getAvailableCapacity() == 0);
        if (classRoomsOutOfSpots) {
            List<ClassRoom> invalidClassRooms = classRooms.stream().filter(classRoom -> classRoom.getAvailableCapacity() == 0).toList();
            throw new NoSpotsAvailableException(invalidClassRooms);
        }
    }

    private void validateInvalidCourseIds(List<ClassRoom> classRooms, SubscriptionDTO subscriptionDTO) {
        boolean invalidCourseIds = classRooms.size() != subscriptionDTO.getClassRoomIds().size();
        if (invalidCourseIds) {
            List<String> invalidClassRoomsIds = subscriptionDTO.getClassRoomIds().stream()
                    .filter(id -> classRooms.stream().noneMatch(classRoom -> classRoom.getId().toString().equals(id)))
                    .toList();
            throw new InvalidCourseTrayException(invalidClassRoomsIds, "The provided class rooms don't exist");
        }
    }

    private void validateActiveEnrollment(String studentId) {
        Term term = termRepository.getCurrentTermEnrollmentByStudentId(studentId);
        if (term != null) {
            throw new InvalidCourseEnrollmentException("The student is already enrolled on the active term");
        }
    }

    private Function<ClassRoom, ClassRoom> getUpdatedClassRoom(SubscriptionDTO subscriptionDTO) {
        return classRoom -> {
            Student student = studentRepository.getReferenceById(subscriptionDTO.getStudentId());
            classRoom.setAvailableCapacity(classRoom.getAvailableCapacity() - 1);
            classRoom.setCurrentEnrollments(classRoom.getCurrentEnrollments() + 1);
            classRoom.addEnrollments(student);
            return classRoom;
        };
    }

    private Function<ClassRoom, CourseGrades> getDefaultGrades(SubscriptionDTO subscriptionDTO) {
        return classRoom -> {
            Student student = studentRepository.getReferenceById(subscriptionDTO.getStudentId());
            return CourseGrades.builder()
                    .student(student)
                    .classRoom(classRoom)
                    .finalGrade(0d).build();
        };
    }

    private void createTermEnrollment(SubscriptionDTO subscriptionDTO) {
        Student student = studentRepository.getReferenceById(subscriptionDTO.getStudentId());
        Term term = termRepository.findByActiveIs(true);
        student.addTermEnrollments(term);
        studentRepository.save(student);
    }

    @Override
    public RegistrationSpotsDTO getEnrollmentInformation(String studentId) {
        validateActiveEnrollment(studentId);
        return buildDTOWithStudentIdAndAvailableCourses(studentId, getAvailableCourses(studentId));
    }

    private List<Course> getAvailableCourses(String studentId) {
        List<Course> successfullyCompletedCourses = getCompletedCoursesBy(studentId);
        List<Course> baseOffering = courseRepository.getBaseOfferingByStudentId(studentId);
        return getAvailableCourses(baseOffering, successfullyCompletedCourses);
    }

    private List<Course> getCompletedCoursesBy(String studentId) {
        return courseGradesRepository
                .findByFinalGradeGreaterThanAndStudentId(2.9d, studentId).stream()
                .map(courseGrade -> courseGrade.getClassRoom().getCourse())
                .toList();
    }

    private List<Course> getAvailableCourses(List<Course> baseOffering, List<Course> completedCourses) {
        return baseOffering.stream()
                .filter(coursesSuccessfullyCompleted(completedCourses))
                .filter(coursesWithoutAllPrerequisitesMet(completedCourses))
                .toList();
    }

    private Predicate<Course> coursesWithoutAllPrerequisitesMet(List<Course> completedCourses) {
        return availableCourse -> availableCourse.getCoursePrerequisites().stream()
                .map(coursePrerequisite -> coursePrerequisite.getCoursePrerequisiteId().getCoursePrerequisite().getId())
                .allMatch(courseCompletion(completedCourses));
    }

    private Predicate<UUID> courseCompletion(List<Course> completedCourses) {
        return (uuid) -> completedCourses.stream().anyMatch(course -> course.getId().equals(uuid));
    }

    private Predicate<Course> coursesSuccessfullyCompleted(List<Course> completedCourses) {
        return availableCourse -> completedCourses.stream().noneMatch(course -> course.getId().equals(availableCourse.getId()));
    }

    private RegistrationSpotsDTO buildDTOWithStudentIdAndAvailableCourses(String studentId, List<Course> availableCourses) {
        Long session = System.currentTimeMillis() + 15 * 60000;
        return RegistrationSpotsDTO.builder()
                .courseDTOList(availableCourses.stream().map(simpleCourseMapper::courseToSimpleCourseDTO).toList())
                .personId(studentId)
                .timestamp(session)
                .build();
    }
}
