package com.first.first_app.Model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;

@Entity
@Table(name = "parents")
public class Parent extends User {

    @ManyToMany
    @JoinTable(
        name = "parent_student",
        joinColumns = @JoinColumn(name = "parent_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> children = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "family_id")
    @JsonIgnore
    private Family family;
    
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserPhone> phones = new ArrayList<>();
    
    public Parent() {
        super();
        this.children = new ArrayList<>();
        this.phones = new ArrayList<>();
        this.role = Role.PARENT;
    }
    
    public List<Student> getChildren() {
        return children;
    }

    public void setChildren(List<Student> children) {
        this.children = children;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family2) {
        this.family = family2;
    }
    
    public List<UserPhone> getPhones() {
        return phones;
    }
    
    public void setPhones(List<UserPhone> phones) {
        this.phones = phones;
    }
}