package com.first.first_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.first.first_app.Model.User;
import com.first.first_app.Repo.UserRepo;
import com.first.first_app.Security.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo ;


public User login(User user) {

    User found = userRepo.findByEmail(user.getEmail());

    if (found != null &&
        found.getPassword().equals(user.getPassword())) {

        return found;
    }

    return null;
}
public User getUserByEmail(String email) {

    return userRepo.findByEmail(email);
}

    public List<User> getAllUsers() { return userRepo.findAll(); }

    public User update(User user, int id) {
        User oldUser = userRepo.findById(id).orElse(null);
        if (oldUser == null) return null;
        
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        
       
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            oldUser.setPassword(user.getPassword());
        }

        if (user.getPhones() != null) {
            oldUser.setPhones(user.getPhones()); 
        }

        return userRepo.save(oldUser);
    }

    public void delete(int id) { userRepo.deleteById(id); }
}