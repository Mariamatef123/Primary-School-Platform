
package com.first.first_app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "user_phones")
public class UserPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must contain 10 to 15 digits")
    @Column(nullable = false)
    private String phoneNumber;
    private String phoneType; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public UserPhone() {}

    public UserPhone(String phoneNumber, String phoneType, User user) {
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.user = user;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPhoneType() { return phoneType; }
    public void setPhoneType(String phoneType) { this.phoneType = phoneType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

