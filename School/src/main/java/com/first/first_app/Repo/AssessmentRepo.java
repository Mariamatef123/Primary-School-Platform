package com.first.first_app.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first.first_app.Model.Assessment;


public interface AssessmentRepo extends JpaRepository<Assessment, Integer>{

   List<Assessment> findBySubjectIdAndIsSummerExamTrue(Integer subjectId);


    
}
