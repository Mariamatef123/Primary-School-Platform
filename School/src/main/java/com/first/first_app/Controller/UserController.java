package com.first.first_app.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.first.first_app.Model.User;
import com.first.first_app.Service.UserService;



@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
  

    

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {
    User loggedInUser = userService.login(user);

    if (loggedInUser != null) {
   
        Map<String, Object> response = new HashMap<>();
        response.put("id", loggedInUser.getId()); 
        response.put("name", loggedInUser.getName());
        response.put("role", loggedInUser.getRole());
        return ResponseEntity.ok(response);
    } else {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Login failed");
        return ResponseEntity.status(401).body(error);
    }
}


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
