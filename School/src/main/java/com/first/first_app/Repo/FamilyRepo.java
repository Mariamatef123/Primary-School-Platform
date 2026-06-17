package com.first.first_app.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.first.first_app.Model.Family;
@Repository
public interface FamilyRepo extends JpaRepository<Family, Integer> {
      @Query("SELECT DISTINCT f FROM Family f LEFT JOIN FETCH f.parents")
    List<Family> findAllWithParents();
    
    @Query("SELECT DISTINCT f FROM Family f LEFT JOIN FETCH f.parents WHERE f.id = :id")
    Optional<Family> findByIdWithParents(Integer id);


}
