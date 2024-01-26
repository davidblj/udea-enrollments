package com.perficient.udea.enrollment.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClassRoomDTO {

    private UUID id;

    private int term;

    private int originalCapacity;

    private String schedule;

    private String recurringDays;

    private String courseId;
}
