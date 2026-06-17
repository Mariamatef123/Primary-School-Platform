package com.first.first_app.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.first.first_app.Model.Level;
import com.first.first_app.Model.Student;
public interface LevelRepo extends JpaRepository<Level,Integer> {

    Optional<Level> findByName(String levelName);
 
}
