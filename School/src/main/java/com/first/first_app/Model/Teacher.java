package com.first.first_app.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @ManyToMany
    @JoinTable(name = "teacher_student", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnore
    private List<Student> students = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "subject_id")
    @JsonBackReference("teacher-subject")
    private Subject subject;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("teacher-score")
    private List<Score> scores = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @JsonManagedReference("teacher-assessment")
    private List<Assessment> assessments = new ArrayList<>();

    public Teacher() {
        super();
        this.role = Role.TEACHER;
    }

    public Teacher(Subject subject, Admin admin, List<Student> students) {
        this.subject = subject;

        this.students = students;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        if (this.subject != null && this.subject != subject) {
            Subject old = this.subject;
            this.subject = null;
            old.setTeacher(null);
        }

        this.subject = subject;
        if (subject != null && subject.getTeacher() != this) {
            subject.setTeacher(this); 
        }
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}