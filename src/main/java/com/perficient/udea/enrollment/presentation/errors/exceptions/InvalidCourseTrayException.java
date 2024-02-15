package com.perficient.udea.enrollment.presentation.errors.exceptions;

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
