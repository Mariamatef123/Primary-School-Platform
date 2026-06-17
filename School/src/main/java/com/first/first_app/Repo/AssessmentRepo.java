package com.first.first_app.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Student;


public interface AssessmentRepo extends JpaRepository<Assessment, Integer>{

   List<Assessment> findBySubjectIdAndIsSummerExamTrue(Integer subjectId);


    
}
