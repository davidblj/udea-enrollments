package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.dtos.CourseDTO;
import com.perficient.udea.enrollment.application.mappers.CourseMapper;
import com.perficient.udea.enrollment.persistence.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    public Page<CourseDTO> listCourses(String syllabusId, Pageable page) {
        return courseRepository.findAllBySyllabusId(UUID.fromString(syllabusId), page)
                .map(courseMapper::courseToCourseDTO);
    }

    public CourseDTO saveCourse(CourseDTO course) {
        return courseMapper.courseToCourseDTO(courseRepository.save(courseMapper.courseDtoToCourse(course)));
    }
}
