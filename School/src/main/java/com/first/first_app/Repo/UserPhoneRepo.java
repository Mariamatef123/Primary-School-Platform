package com.first.first_app.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.first.first_app.Model.UserPhone;

import jakarta.transaction.Transactional;

public interface UserPhoneRepo extends JpaRepository<UserPhone, Long> {
    List<UserPhone> findByUser_Id(int userId);
@Modifying
@Transactional
@Query("DELETE FROM UserPhone p WHERE p.user.id = :userId")
void deleteByUserId(@Param("userId") int userId);
}