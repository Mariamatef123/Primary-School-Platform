package com.first.first_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
        @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // Public pages
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/index").setViewName("forward:/index.html");
        registry.addViewController("/about").setViewName("forward:/about.html");
        registry.addViewController("/contact").setViewName("forward:/contact.html");
        registry.addViewController("/subject").setViewName("forward:/subject.html");
        registry.addViewController("/login").setViewName("forward:/login.html");


        // Admin pages
        registry.addViewController("/dashboard-admin").setViewName("forward:/dashboard-admin.html");
        registry.addViewController("/manage-student").setViewName("forward:/manage-student.html");
        registry.addViewController("/manage-teacher").setViewName("forward:/manage-teacher.html");
        registry.addViewController("/manage-parent").setViewName("forward:/manage-parent.html");
        registry.addViewController("/manage-levels").setViewName("forward:/manage-levels.html");
        registry.addViewController("/manage-subjects").setViewName("forward:/manage-subjects.html");

        // Teacher pages
        registry.addViewController("/dashboard-teacher").setViewName("forward:/dashboard-teacher.html");
        registry.addViewController("/manage-assignments").setViewName("forward:/manage-assignments.html");
        registry.addViewController("/manage-quizzes").setViewName("forward:/manage-quizzes.html");
        registry.addViewController("/view-student-grades").setViewName("forward:/view-student-grades.html");
        registry.addViewController("/teacher-profile").setViewName("forward:/teacher-profile.html");
        registry.addViewController("/submissions").setViewName("forward:/submissions.html");

        // Parent pages
        registry.addViewController("/dashboard-parent").setViewName("forward:/dashboard-parent.html");
        registry.addViewController("/view-child").setViewName("forward:/view-child.html");
        registry.addViewController("/select-child").setViewName("forward:/select-child.html");
        registry.addViewController("/parent-assignments").setViewName("forward:/parent-assignments.html");

        // Student pages
        registry.addViewController("/dashboard-student").setViewName("forward:/dashboard-student.html");
        registry.addViewController("/student-assignments").setViewName("forward:/student-assignments.html");
        registry.addViewController("/student-quizzes").setViewName("forward:/student-quizzes.html");
        registry.addViewController("/student-grades").setViewName("forward:/student-grades.html");
        registry.addViewController("/take-Exam").setViewName("forward:/take-Exam.html");
        registry.addViewController("/model-answer").setViewName("forward:/model-answer.html");
        // Add/Edit pages (forms)
        registry.addViewController("/add-parent").setViewName("forward:/add-parent.html");
        registry.addViewController("/add-student").setViewName("forward:/add-student.html");
        registry.addViewController("/add-teacher").setViewName("forward:/add-teacher.html");
        registry.addViewController("/create-quiz").setViewName("forward:/create-quiz.html");
        registry.addViewController("/create-assignment").setViewName("forward:/create-assignment.html");
        registry.addViewController("/create-exam").setViewName("forward:/create-exam.html");
    }
}
