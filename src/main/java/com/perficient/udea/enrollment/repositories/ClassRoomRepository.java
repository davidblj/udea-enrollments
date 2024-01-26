package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, UUID> {
}
