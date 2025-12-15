package com.first.first_app.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "levels")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public static final int SUBJECT_COUNT = 7;
    public static final int LEVEL_COUNT = 6;
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "level", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE },                                                                                               // REMOVE
            orphanRemoval = true)
    @JsonIgnore
    private List<Subject> subjects = new ArrayList<>();
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    public Level() {
    }

    public Level(String name) {
        this.name = name;
    }

    public int getId() {
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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects.clear();
        if (subjects != null) {
            for (Subject s : subjects) {
                addSubject(s);
            }
        }
    }

    public void addSubject(Subject subject) {
        if (subjects.size() >= SUBJECT_COUNT) {
            throw new IllegalArgumentException("Cannot add more than " + SUBJECT_COUNT + " subjects to a level");
        }
        if (!subjects.contains(subject)) {
            subjects.add(subject);
            subject.setLevel(this);
        }
    }

    public void removeSubject(Subject subject) {
        if (subjects.remove(subject)) {
            subject.setLevel(null);
        }
    }
}
