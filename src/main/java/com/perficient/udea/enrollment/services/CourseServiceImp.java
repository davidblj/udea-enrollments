package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.mappers.CourseMapper;
import com.perficient.udea.enrollment.DTOs.CourseDTO;
import com.perficient.udea.enrollment.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImp implements CourseService {

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    @Override
    public List<CourseDTO> listCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::courseToCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO saveCourse(CourseDTO course) {
        return courseMapper.courseToCourseDTO(courseRepository.save(courseMapper.courseDtoToCourse(course)));
    }
}
