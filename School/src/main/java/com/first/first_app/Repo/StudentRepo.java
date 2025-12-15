package com.first.first_app.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.first.first_app.Model.Student;




public interface StudentRepo extends JpaRepository<Student, Integer>{
@Query("SELECT s FROM Student s")
List<Student> findAllWithSubjectsAndTeachers();
List<Student> findByLevel(com.first.first_app.Model.Level level);
List<Student> findTop5ByOrderByIdDesc();  
}
