package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.Pensum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PensumRepository extends JpaRepository<Pensum, UUID> {
}
