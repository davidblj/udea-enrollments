package com.perficient.udea.enrollment.bootstrap;

import com.perficient.udea.enrollment.entities.*;
import com.perficient.udea.enrollment.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final CourseInstanceRepository courseInstanceRepository;
    private final CoursePrerequisiteRepository coursePrerequisiteRepository;
    private final PensumRepository pensumRepository;
    private final PersonRepository personRepository;
    private final CourseGradesRepository courseGradesRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {

        if (pensumRepository.count() == 0) {

            Pensum computerSciencePensum = Pensum.builder()
                    .career("Computer Science Engineering")
                    .totalCredits(210)
                    .minimumTotalCredits(150)
                    .build();
            pensumRepository.save(computerSciencePensum);

            // Courses
            Course calculusCourse = Course.builder()
                    .courseName("Differential Calculus")
                    .pensum(computerSciencePensum)
                    .semester(2)
                    .scienceField("Mathematics")
                    .build();
            Course integralCalculusCourse = Course.builder()
                    .courseName("Integral Calculus")
                    .pensum(computerSciencePensum)
                    .semester(3)
                    .scienceField("Mathematics")
                    .build();
            Course physicsFundamentals = Course.builder()
                    .courseName("Physics Fundamentals")
                    .pensum(computerSciencePensum)
                    .semester(3)
                    .scienceField("Physics")
                    .build();
            Course algebra = Course.builder()
                    .courseName("Algebra")
                    .pensum(computerSciencePensum)
                    .semester(1)
                    .build();
            Course computerProgramming = Course.builder()
                    .courseName("Computer Programming")
                    .pensum(computerSciencePensum)
                    .semester(1)
                    .scienceField("Software methods and Technologies")
                    .build();
            Course objectOrientedProgramming = Course.builder()
                    .courseName("Computer Programming")
                    .pensum(computerSciencePensum)
                    .semester(2)
                    .scienceField("Software methods and Technologies")
                    .build();

            courseRepository.save(calculusCourse);
            courseRepository.save(integralCalculusCourse);
            courseRepository.save(physicsFundamentals);
            courseRepository.save(algebra);
            courseRepository.save(computerProgramming);
            courseRepository.save(objectOrientedProgramming);

            // Instances
            CourseInstance intCalcMondayWednesday = CourseInstance.builder()
                    .course(integralCalculusCourse)
                    .availableCapacity(40)
                    .build();

            CourseInstance diffCalcMondayWednesday = CourseInstance.builder()
                    .course(calculusCourse)
                    .availableCapacity(40)
                    .build();

            CourseInstance algebraMondayWednesday = CourseInstance.builder()
                    .course(algebra)
                    .availableCapacity(20)
                    .build();

            CourseInstance physicsFundamentalsMondayWednesday = CourseInstance.builder()
                    .course(physicsFundamentals)
                    .availableCapacity(20)
                    .build();

            CourseInstance computerProgrammingMondayWednesday = CourseInstance.builder()
                    .course(computerProgramming)
                    .availableCapacity(25)
                    .build();

            CourseInstance objectOrientedProgrammingMondayWednesday = CourseInstance.builder()
                    .course(objectOrientedProgramming)
                    .availableCapacity(25)
                    .build();

            courseInstanceRepository.save(diffCalcMondayWednesday);
            courseInstanceRepository.save(intCalcMondayWednesday);
            courseInstanceRepository.save(algebraMondayWednesday);
            courseInstanceRepository.save(physicsFundamentalsMondayWednesday);
            courseInstanceRepository.save(computerProgrammingMondayWednesday);
            courseInstanceRepository.save(objectOrientedProgrammingMondayWednesday);

            // Prerequisites
            CoursePrerequisiteId idDiffCalcAndAlgebra = CoursePrerequisiteId.builder()
                    .course(calculusCourse)
                    .coursePrerequisite(algebra)
                    .build();
            CoursePrerequisite prerequisiteDifferentialCalculus1 = CoursePrerequisite.builder()
                    .coursePrerequisiteId(idDiffCalcAndAlgebra)
                    .semester(202301)
                    .build();

            CoursePrerequisiteId idIntCalcAndDiffCalc = CoursePrerequisiteId.builder()
                    .course(integralCalculusCourse)
                    .coursePrerequisite(calculusCourse)
                    .build();
            CoursePrerequisite prerequisiteIntegralCalculus1 = CoursePrerequisite.builder()
                    .coursePrerequisiteId(idIntCalcAndDiffCalc)
                    .semester(202301)
                    .build();

            CoursePrerequisiteId idObjOriProAndCompPro = CoursePrerequisiteId.builder()
                    .course(objectOrientedProgramming)
                    .coursePrerequisite(computerProgramming)
                    .build();
            CoursePrerequisite prerequisiteObjOriProAndCompPro1 = CoursePrerequisite.builder()
                    .coursePrerequisiteId(idObjOriProAndCompPro)
                    .semester(202301)
                    .build();

            coursePrerequisiteRepository.save(prerequisiteIntegralCalculus1);
            coursePrerequisiteRepository.save(prerequisiteDifferentialCalculus1);
            coursePrerequisiteRepository.save(prerequisiteObjOriProAndCompPro1);

            // students
            Student student1 = Student.builder()
                    .semester(3)
                    .pensum(computerSciencePensum)
                    .stratum(3)
                    .build();

            // parent class
            student1.setId("1152209135");
            student1.setEmail("david.blj95@gmail.com");
            student1.setFullName("David Jaramillo bolivar");
            student1.setPhoneNumber("3003102703");

            personRepository.save(student1);

            // grades
            CourseGrades courseGrades1 = CourseGrades.builder()
                    .finalGrade(3.5)
                    .student(student1)
                    .courseInstance(diffCalcMondayWednesday)
                    .build();
            CourseGrades courseGrades2 = CourseGrades.builder()
                    .finalGrade(4.2)
                    .student(student1)
                    .courseInstance(algebraMondayWednesday)
                    .build();

            courseGradesRepository.save(courseGrades1);
            courseGradesRepository.save(courseGrades2);
        }
    }
}
