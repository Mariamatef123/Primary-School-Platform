package com.first.first_app.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first.first_app.Model.Question;

public interface QuestionRepo extends JpaRepository<Question,Integer>{



}
