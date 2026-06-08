package com.first.first_app.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.first.first_app.Model.Level;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Teacher;
import java.util.Optional;


public interface TeacherRepo extends JpaRepository<Teacher, Integer>{
	Optional<Teacher> findByEmail(String email);

    Optional<Level> findBySubject(Subject subject);

}
