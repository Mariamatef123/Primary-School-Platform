package com.first.first_app.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList; 
import java.util.List; 
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    protected String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(unique = true)
    protected String email;

    @NotBlank(message = "Password is required")
    @Size(min = 12, message = "Password must be at least 12 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$", message = "Password must be at least 12 characters long and include numbers, lowercase, uppercase, and special characters.")
    protected String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    protected List<UserPhone> phones = new ArrayList<>();

    public User() {
        this.name = "name";
        this.email = "email";
        this.password = "password";
        this.phones = new ArrayList();
    }

    public User(String email2, String password2) {
      this.email=email2;
      this.password=password2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserPhone> getPhones() {
        return phones;
    }

    public void setPhones(List<UserPhone> phones2) {
        this.phones.clear();
        if (phones2 != null) {
            for (UserPhone phone : phones) {
                addPhone(phone);
            }
        }
    }

    public void addPhone(UserPhone phone) {
        if (phone != null && !this.phones.contains(phone)) {
            this.phones.add(phone);
            phone.setUser(this);
        }
    }

    public void removePhone(UserPhone phone) {
        if (this.phones.remove(phone)) {
            phone.setUser(null);
        }
    }

    public Role getRole() {
        return role;
    }


}