package com.first.first_app.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.first.first_app.Model.Teacher;


public interface TeacherRepo extends JpaRepository<Teacher, Integer>{

}
