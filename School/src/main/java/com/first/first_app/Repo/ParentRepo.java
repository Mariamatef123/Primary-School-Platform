package com.first.first_app.Repo;

import com.first.first_app.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParentRepo extends JpaRepository<Parent, Integer> {
	Optional<Parent> findByEmail(String email);

@Query("SELECT p FROM Parent p LEFT JOIN FETCH p.children")
List<Parent> findAllWithChildren();
}
