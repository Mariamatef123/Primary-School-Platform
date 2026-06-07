package com.first.first_app.Repo;

import org.springframework.stereotype.Repository;

import com.first.first_app.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

User existsByEmail(String email);

User findByEmail(String email);


}
