package com.perficient.udea.enrollment.presentation.errors.exceptions;

import com.perficient.udea.enrollment.persistence.entities.ClassRoom;
import lombok.Getter;

import java.util.List;

@Getter
public class NoSpotsAvailableException extends ValidationException {

    List<ClassRoom> classRoomsOutOfSpots;

    public NoSpotsAvailableException(List<ClassRoom> classRoomsOutOfSpots, String message) {
        super(message);
        this.classRoomsOutOfSpots = classRoomsOutOfSpots;
    }
}
