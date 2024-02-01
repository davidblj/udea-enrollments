package com.perficient.udea.enrollment.DTOs;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClassRoomDTO {

    private UUID id;

    @Digits(integer = 6, fraction = 0)
    private int term;

    @NotNull
    @Min(value = 1)
    @Max(value = 50)
    private int originalCapacity;

    @Pattern(regexp="^(1[0-2]|0?[1-9])-([1-5]?[0-9])\\s+(AM|PM)$")
    private String schedule;

    @Pattern(regexp="^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)-(Mon|Tue|Wed|Thu|Fri|Sat|Sun)$")
    private String recurringDays;

    @org.hibernate.validator.constraints.UUID(version = 1)
    private String courseId;
}
