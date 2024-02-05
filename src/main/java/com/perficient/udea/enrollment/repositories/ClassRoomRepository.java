package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.ClassRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.UUID;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, UUID> {

    List<ClassRoom> findAllByCourseId(UUID id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<ClassRoom> getAllByIdIn(List<UUID> ids);
}
