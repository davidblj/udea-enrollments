package com.perficient.udea.enrollment.errors.exceptions;

import com.perficient.udea.enrollment.entities.ClassRoom;
import lombok.Getter;

import java.util.List;

@Getter
public class InvalidCourseTrayException extends ValidationException {

    List<String> invalidClassRoomsIds;

    public InvalidCourseTrayException(List<String> invalidClassRoomsIds, String message) {

        super(message);
        this.invalidClassRoomsIds = invalidClassRoomsIds;
    }
}
