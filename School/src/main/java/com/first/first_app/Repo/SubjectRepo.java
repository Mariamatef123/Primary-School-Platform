package com.first.first_app.Repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Subject;


public interface SubjectRepo extends JpaRepository<Subject, Integer>{

    Long countByLevelId(int id);

    long countByLevelIdAndTerm(int levelId, Term term);



}
