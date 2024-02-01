package com.perficient.udea.enrollment.errors.exceptions;

import com.perficient.udea.enrollment.entities.ClassRoom;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class NoSpotsAvailableException extends ValidationException {

    List<ClassRoom> classRoomsOutOfSpots;

    public NoSpotsAvailableException(List<ClassRoom> classRoomsOutOfSpots) {

        super("The provided classrooms don't have enough spots to subscribe new students. They were taken while" +
                " the request was being processed or while the user was completing the form");
        this.classRoomsOutOfSpots = classRoomsOutOfSpots;
    }
}
