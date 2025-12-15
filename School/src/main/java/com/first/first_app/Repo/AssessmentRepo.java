package com.first.first_app.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first.first_app.Model.Assessment;


public interface AssessmentRepo extends JpaRepository<Assessment, Integer>{
    
}
