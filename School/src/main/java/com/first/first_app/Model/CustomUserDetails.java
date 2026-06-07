package com.first.first_app.Model;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class CustomUserDetails extends User {

 
    private final int id; 

    public CustomUserDetails(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
      
        super(username, password, authorities);
   
        this.id = id;
    }

 
    public int getId() { 
        return id;
    }
    

}