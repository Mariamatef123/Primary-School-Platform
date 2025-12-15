package com.first.first_app.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "students")
public class Student extends User {
    @Size(min = 14, max = 14)
    @Column(unique = true, nullable = false)
    private String ssn;

    @NotNull
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToMany(mappedBy = "children")
    @JsonIgnore
    private List<Parent> parents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToMany
    @JsonIgnore
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Score> scores = new ArrayList<>();

    private List<Integer> parentIds; // received from frontend


    public Student() {
        super();
        this.role = Role.STUDENT;
    }

    public Student(String ssn, LocalDate dateOfBirth, Level level) {
        this.ssn = ssn;
        this.dateOfBirth = dateOfBirth;
        this.level = level;
    }

    @AssertTrue(message = "Student age must be between 7 and 10 years")
    private boolean isAgeValid() {
        if (dateOfBirth == null)
            return true;
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= 7 && age <= 10;
    }


    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Integer> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<Integer> parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getLevelId() { 
        return level.getId();
    }


    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    
    public void addTeacher(Teacher teacher) {
        if (!teachers.contains(teacher)) {
            teachers.add(teacher);
        }
    }

    public void addParent(Parent parent) {
        if (!parents.contains(parent)) {
            parents.add(parent);
        }
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
    }

    public List<Subject> getSubjects() {
        if (level != null) {
            return level.getSubjects();
        }
        return new ArrayList<>();
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
