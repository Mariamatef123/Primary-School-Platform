package com.first.first_app.Repo;

import com.first.first_app.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParentRepo extends JpaRepository<Parent, Integer> {
	Optional<Parent> findByEmail(String email);

}
