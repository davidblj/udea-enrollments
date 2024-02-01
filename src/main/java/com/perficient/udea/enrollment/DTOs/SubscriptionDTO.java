package com.perficient.udea.enrollment.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionDTO {
    private String studentId;
    private Long timestamp;
    private List<String> classRoomIds;
}
