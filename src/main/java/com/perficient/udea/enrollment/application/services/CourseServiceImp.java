package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.DTOs.CourseDTO;
import com.perficient.udea.enrollment.application.mappers.CourseMapper;
import com.perficient.udea.enrollment.persistence.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImp implements CourseService {

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    @Override
    public Page<CourseDTO> listCourses(String syllabusId, int page, int size) {
        Sort sort = Sort.by(Sort.Order.asc("courseName"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return courseRepository.findAllBySyllabusId(UUID.fromString(syllabusId), pageRequest)
                .map(courseMapper::courseToCourseDTO);
    }

    @Override
    public CourseDTO saveCourse(CourseDTO course) {
        return courseMapper.courseToCourseDTO(courseRepository.save(courseMapper.courseDtoToCourse(course)));
    }
}
