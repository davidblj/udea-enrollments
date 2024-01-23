package com.perficient.udea.enrollment.bootstrap;

import com.perficient.udea.enrollment.entities.Course;
import com.perficient.udea.enrollment.entities.Pensum;
import com.perficient.udea.enrollment.repositories.CourseRepository;
import com.perficient.udea.enrollment.repositories.PensumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final PensumRepository pensumRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {

        if (courseRepository.count() == 0) {
            Course calculusCourse = Course.builder()
                    .courseName("Calculus")
                    .build();

            Course advancedPhysics = Course.builder()
                    .courseName("Physics")
                    .build();

            courseRepository.save(calculusCourse);
            courseRepository.save(advancedPhysics);
        }

        if (pensumRepository.count() == 0) {
            Pensum computerSciencePensum = Pensum.builder()
                    .career("Computer Science Engineering")
                    .totalCredits(210)
                    .minimumTotalCredits(150)
                    .build();
            pensumRepository.save(computerSciencePensum);
        }
    }
}
