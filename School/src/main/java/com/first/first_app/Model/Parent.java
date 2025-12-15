package com.first.first_app.Model;
import jakarta.persistence.*;
import java.util.List;



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

    public Parent() {
        super();
        this.children = new ArrayList<>();
        this.role = Role.PARENT;
    }
    
    
    public List<Student> getChildren() {
        return children;
    }

    public void setChildren(List<Student> children) {
        this.children = children;
    }
    
}