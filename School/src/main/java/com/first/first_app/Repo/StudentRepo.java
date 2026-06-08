package com.first.first_app.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.first.first_app.Model.Level;
import com.first.first_app.Model.Student;




public interface StudentRepo extends JpaRepository<Student, Integer>{
@Query("SELECT s FROM Student s")
List<Student> findAllWithSubjectsAndTeachers();
List<Student> findByLevel(com.first.first_app.Model.Level level);
List<Student> findTop5ByOrderByIdDesc();  
	Optional<Student> findByEmail(String email);
    Optional<Level> findBySsn(String string);
}
