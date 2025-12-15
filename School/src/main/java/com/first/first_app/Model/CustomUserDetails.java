package com.first.first_app.Model;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class CustomUserDetails extends User {

    // Add the ID field as a property
    private final int id; 

    public CustomUserDetails(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        // Call the parent constructor with standard fields
        super(username, password, authorities);
        // Initialize our custom field
        this.id = id;
    }

    // Add a public getter method for the 'id' field
    // This is what the SpEL expression `authentication.principal.id` needs to call.
    public int getId() { 
        return id;
    }
    
    // You can add other custom fields/getters if needed later (e.g., firstName, etc.)
}