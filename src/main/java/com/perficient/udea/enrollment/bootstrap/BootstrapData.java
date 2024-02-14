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
    private final ClassRoomRepository courseInstanceRepository;
    private final CoursePrerequisiteRepository coursePrerequisiteRepository;
    private final SyllabusRepository syllabusRepository;
    private final PersonRepository personRepository;
    private final CourseGradesRepository courseGradesRepository;
    private final TermRepository termRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {

        /*
        if (syllabusRepository.count() == 0) {

            Syllabus computerScienceSyllabus = Syllabus.builder()
                    .career("Computer Science Engineering")
                    .totalCredits(210)
                    .minimumTotalCredits(150)
                    .build();
            syllabusRepository.save(computerScienceSyllabus);

            Term term202401 = Term.builder().term("202401").active(true).build();
            Term term202302 = Term.builder().term("202302").active(false).build();

            termRepository.save(term202401);
            termRepository.save(term202302);

            // Courses
            Course calculusCourse = Course.builder()
                    .courseName("Differential Calculus")
                    .syllabus(computerScienceSyllabus)
                    .semester(2)
                    .scienceField("Mathematics")
                    .build();
            Course integralCalculusCourse = Course.builder()
                    .courseName("Integral Calculus")
                    .syllabus(computerScienceSyllabus)
                    .semester(3)
                    .scienceField("Mathematics")
                    .build();
            Course physicsFundamentals = Course.builder()
                    .courseName("Physics Fundamentals")
                    .syllabus(computerScienceSyllabus)
                    .semester(3)
                    .scienceField("Physics")
                    .build();
            Course algebra = Course.builder()
                    .courseName("Algebra")
                    .syllabus(computerScienceSyllabus)
                    .semester(1)
                    .scienceField("Mathematics")
                    .build();
            Course computerProgramming = Course.builder()
                    .courseName("Computer Programming")
                    .syllabus(computerScienceSyllabus)
                    .semester(1)
                    .scienceField("Software methods and Technologies")
                    .build();
            Course objectOrientedProgramming = Course.builder()
                    .courseName("Object Oriented Programming")
                    .syllabus(computerScienceSyllabus)
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
            ClassRoom intCalcMondayWednesday = ClassRoom.builder()
                    .course(integralCalculusCourse)
                    .availableCapacity(40)
                    .build();

            ClassRoom diffCalcMondayWednesday = ClassRoom.builder()
                    .course(calculusCourse)
                    .availableCapacity(40)
                    .build();

            ClassRoom algebraMondayWednesday = ClassRoom.builder()
                    .course(algebra)
                    .availableCapacity(20)
                    .build();

            ClassRoom physicsFundamentalsMondayWednesday = ClassRoom.builder()
                    .course(physicsFundamentals)
                    .availableCapacity(20)
                    .build();

            ClassRoom computerProgrammingMondayWednesday = ClassRoom.builder()
                    .course(computerProgramming)
                    .availableCapacity(25)
                    .build();

            ClassRoom objectOrientedProgrammingMondayWednesday = ClassRoom.builder()
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
                    .build();

            CoursePrerequisiteId idIntCalcAndDiffCalc = CoursePrerequisiteId.builder()
                    .course(integralCalculusCourse)
                    .coursePrerequisite(calculusCourse)
                    .build();
            CoursePrerequisite prerequisiteIntegralCalculus1 = CoursePrerequisite.builder()
                    .coursePrerequisiteId(idIntCalcAndDiffCalc)
                    .build();

            CoursePrerequisiteId idObjOriProAndCompPro = CoursePrerequisiteId.builder()
                    .course(objectOrientedProgramming)
                    .coursePrerequisite(computerProgramming)
                    .build();
            CoursePrerequisite prerequisiteObjOriProAndCompPro1 = CoursePrerequisite.builder()
                    .coursePrerequisiteId(idObjOriProAndCompPro)
                    .build();

            coursePrerequisiteRepository.save(prerequisiteIntegralCalculus1);
            coursePrerequisiteRepository.save(prerequisiteDifferentialCalculus1);
            coursePrerequisiteRepository.save(prerequisiteObjOriProAndCompPro1);

            // students
            Student student1 = Student.builder()
                    .semester(3)
                    .syllabus(computerScienceSyllabus)
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
                    .classRoom(diffCalcMondayWednesday)
                    .build();
            CourseGrades courseGrades2 = CourseGrades.builder()
                    .finalGrade(4.2)
                    .student(student1)
                    .classRoom(algebraMondayWednesday)
                    .build();

            courseGradesRepository.save(courseGrades1);
            courseGradesRepository.save(courseGrades2);
        }
        */
    }
}
