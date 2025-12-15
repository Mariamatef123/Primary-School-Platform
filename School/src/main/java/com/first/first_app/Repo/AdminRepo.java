package com.first.first_app.Repo;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.first.first_app.Model.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer> {
    
}