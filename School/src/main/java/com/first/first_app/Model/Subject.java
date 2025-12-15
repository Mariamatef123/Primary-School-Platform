package com.first.first_app.Model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @NotNull(message = "Subject code is required")
    @Min(value = 1, message = "Subject code must be greater than 0")
    private int code;

    @NotBlank(message = "Subject name is required")
    @Size(min = 2, max = 100, message = "Subject name must be between 2 and 100 characters")
    private String name;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("subject-assessment")
    private List<Assessment> assessments = new ArrayList<>();

    @OneToOne(mappedBy = "subject", cascade = CascadeType.PERSIST)
    @JsonManagedReference("teacher-subject")
    private Teacher teacher;

    public Subject() {
        this.assessments = new ArrayList<>();
    }

    public Subject(int code, String name, Level level) {
        this();
        this.code = code;
        this.name = name;
        this.level = level;
    }

    public void addAssessment(Assessment assessment) {
        AssessmentType newType = assessment.getType();
        int requiredMax = newType.getNoOfAssessment();

        long currentCount = this.assessments.stream()
                .filter(a -> a.getType().equals(newType))
                .count();

        if (currentCount >= requiredMax) {
            throw new IllegalArgumentException(
                    "Cannot add more than " + requiredMax + " assessments of type " + newType + " to this subject.");
        }

        this.assessments.add(assessment);
        assessment.setSubject(this);
    }

    public void removeAssessment(Assessment assessment) {
        this.assessments.remove(assessment);
        assessment.setSubject(null);
    }

    public void clearAssessments() {
        for (Assessment a : new ArrayList<>(assessments)) {
            removeAssessment(a);
        }
    }

    public void setTeacher(Teacher teacher) {
        if (this.teacher != null && this.teacher != teacher) {
            Teacher old = this.teacher;
            this.teacher = null;
            old.setSubject(null);
        }

        this.teacher = teacher;

        if (teacher != null && teacher.getSubject() != this) {
            teacher.setSubject(this);
        }
    }

    public void removeTeacher() {
        if (this.teacher != null) {
            Teacher old = this.teacher;
            this.teacher = null;
            old.setSubject(null);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessment) {
        this.assessments = assessment;
    }

    public Teacher getTeacher() {
        return teacher;
    }
}
