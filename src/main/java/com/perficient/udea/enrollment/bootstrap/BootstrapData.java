package com.perficient.udea.enrollment.bootstrap;

import com.perficient.udea.enrollment.entities.Course;
import com.perficient.udea.enrollment.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final CourseRepository courseRepository;

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
    }
}
