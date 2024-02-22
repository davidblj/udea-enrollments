package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.ClassRoom;
import com.perficient.udea.enrollment.persistence.entities.Course;
import com.perficient.udea.enrollment.persistence.entities.Term;
import com.perficient.udea.enrollment.persistence.repositories.TermRepository;
import com.perficient.udea.enrollment.presentation.errors.exceptions.EnrollmentInvalidSessionException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.ActiveEnrollmentException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.InvalidCourseTrayException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.NoSpotsAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class EnrollmentValidator {

    private final CoursePool coursePool;
    private final ClassRoomPool classRoomPool;
    private final TermRepository termRepository;

    private String studentId;
    private List<UUID> classRoomIds;
    private List<ClassRoom> classRoomsToEnroll;
    private long sessionTimeStamp;

    public void validateOpening(String studentId) {
        this.studentId = studentId;
        validateActiveEnrollment();
    }

    public void validateSubscription(SubscriptionDTO subscriptionDTO) {
        setData(subscriptionDTO);
        validateSession();
        validateActiveEnrollment();
        validateClassRoomIds();
        validateClassRoomSpots();
        validateCourseSubscriptions();
    }
    
    private void setData(SubscriptionDTO subscriptionDTO) {
        this.studentId = subscriptionDTO.getStudentId();
        this.sessionTimeStamp = subscriptionDTO.getTimestamp();
        this.classRoomIds = subscriptionDTO.getClassRoomIds().stream().map(UUID::fromString).toList();
        this.classRoomsToEnroll = classRoomPool.getClassRooms(classRoomIds);
    }

    private void validateSession() {
        if (System.currentTimeMillis() > sessionTimeStamp) {
            throw new EnrollmentInvalidSessionException("The sessions is finished, the user took more than 15 minutes filling the form");
        }
    }

    private void validateActiveEnrollment() {
        Term term = termRepository.getCurrentTermEnrollmentByStudentId(studentId);
        if (term != null) {
            throw new ActiveEnrollmentException("The student is already enrolled on the active term");
        }
    }

    private void validateClassRoomIds() {
        boolean invalidCourseIds = this.classRoomsToEnroll.size() != this.classRoomIds.size();
        if (invalidCourseIds) {
            List<String> invalidClassRoomsIds = this.classRoomIds.stream()
                    .map(UUID::toString)
                    .filter(invalidClassRoom())
                    .toList();
            throw new InvalidCourseTrayException(invalidClassRoomsIds, "The provided class rooms don't exist");
        }
    }

    private Predicate<String> invalidClassRoom() {
        return classRoomToEnrollId -> classRoomsToEnroll.stream()
                .noneMatch(classRoom -> classRoom.getId().toString().equals(classRoomToEnrollId));
    }

    private void validateClassRoomSpots() {
        List<ClassRoom> classRoomsOutOfSpots = classRoomsToEnroll.stream().filter(classRoom -> classRoom.getAvailableCapacity() == 0).toList();
        if (!classRoomsOutOfSpots.isEmpty()) {
            throw new NoSpotsAvailableException(classRoomsOutOfSpots, "The provided classrooms don't have enough spots to subscribe new students. They were taken while the request was being processed or while the user was completing the form");
        }
    }

    private void validateCourseSubscriptions() {

        List<Course> availableCourses = coursePool.getAvailableCourses(this.studentId);
        List<ClassRoom> invalidEnrollments = classRoomsToEnroll.stream()
                .filter(invalidEnrollments(availableCourses))
                .toList();
        if (!invalidEnrollments.isEmpty()) {
            List<String> invalidClassRoomsIds = invalidEnrollments.stream().map(classRoom -> classRoom.getId().toString()).toList();
            throw new InvalidCourseTrayException(invalidClassRoomsIds, "The provided course class rooms are not yet unlocked because the user either has not met the requirements or they are already completed");
        }
    }

                  private Predicate<ClassRoom> invalidEnrollments(List<Course> availableCourses) {
        return classRoomToValidate -> availableCourses.stream()
                .noneMatch(course -> course.getId().equals(classRoomToValidate.getCourse().getId()));
    }
}
