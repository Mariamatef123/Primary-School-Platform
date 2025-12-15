package com.first.first_app.Repo;

import org.springframework.data.jpa.repository.JpaRepository;


import com.first.first_app.Model.Level;
public interface LevelRepo extends JpaRepository<Level,Integer> {
 
}
