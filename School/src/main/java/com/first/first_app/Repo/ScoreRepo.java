package com.first.first_app.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.first.first_app.Model.Score;

public interface ScoreRepo extends JpaRepository<Score,Integer> {
        @Query("SELECT s FROM Score s " +
       "JOIN FETCH s.assessment a " +
       "JOIN FETCH a.teacher t " +
       "JOIN FETCH s.student st " +
       "WHERE t.id = :teacherId")
List<Score> findByTeacherId(@Param("teacherId") int teacherId);

}
