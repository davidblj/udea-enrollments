package com.perficient.udea.enrollment.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

@Data
public class SubscriptionDTO {

    @NotBlank
    private String studentId;

    @NotNull
    private Long timestamp;

    @NotEmpty
    private List<@UUID(version = 1) String> classRoomIds;
}
