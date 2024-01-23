package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
