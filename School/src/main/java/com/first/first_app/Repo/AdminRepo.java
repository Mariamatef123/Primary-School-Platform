package com.first.first_app.Repo;




import org.springframework.data.jpa.repository.JpaRepository;
import com.first.first_app.Model.Admin;
import com.first.first_app.Model.User;

public interface AdminRepo extends JpaRepository<Admin, Integer> {

    User save(User familyMember);
    
}