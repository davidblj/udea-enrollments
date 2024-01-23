package com.perficient.udea.enrollment.mappers;

import com.perficient.udea.enrollment.entities.Course;
import com.perficient.udea.enrollment.entities.CoursePrerequisite;
import com.perficient.udea.enrollment.entities.CoursePrerequisiteId;
import com.perficient.udea.enrollment.entities.Pensum;
import com.perficient.udea.enrollment.model.CourseDTO;
import com.perficient.udea.enrollment.model.CoursePrerequisiteDTO;
import com.perficient.udea.enrollment.model.PensumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

//@Mapper(componentModel = "spring")
@Mapper()
public interface CourseMapper {

    @Mapping( target = "pensum.id", source = "pensumId" )
    Course courseDtoToCourse(CourseDTO courseDto);
    CourseDTO courseToCourseDTO(Course course);

    default CoursePrerequisiteId map(String value) {
        Course prerequisite = Course.builder().id(UUID.fromString(value)).build();
        return CoursePrerequisiteId.builder().coursePrerequisite(prerequisite).build();
    }

    default String map(CoursePrerequisiteId value) {
        return value.getCourse().getId().toString();
    }

    /*default Pensum pensumDTOToPensum(PensumDTO pensumDTO) {
        Pensum.PensumBuilder pensumBuilder = Pensum.builder();
        pensumBuilder.id( pensumDTO.getId() );
        return pensumBuilder.build();
    }

    default CoursePrerequisite coursePrerequisiteDTOToCoursePrerequisite(CoursePrerequisiteDTO coursePrerequisiteDTO, Course course) {
        if (coursePrerequisiteDTO == null) {
            return null;
        } else {
            CoursePrerequisite.CoursePrerequisiteBuilder coursePrerequisite = CoursePrerequisite.builder();
            Course mainCourse = Course.builder().id(coursePrerequisiteDTO.getCourse().getId()).build();
            Course prerequisite = Course.builder().id(coursePrerequisiteDTO.getCoursePrerequisite().getId()).build();
            CoursePrerequisiteId coursePrerequisiteId = CoursePrerequisiteId.builder().course(mainCourse).coursePrerequisite(prerequisite).build();
            coursePrerequisite.coursePrerequisiteId(coursePrerequisiteId);
            return coursePrerequisite.build();
        }
    }*/
}
