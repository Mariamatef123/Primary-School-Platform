package com.first.first_app.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.first_app.Enum.Term;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "levels")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public static final int SUBJECTS_PER_TERM = 7;
    public static final int LEVEL_COUNT = 6;
    
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "level", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    @JsonIgnore
    private List<Subject> subjects = new ArrayList<>();
    
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
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
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        
        Term term = subject.getTerm();
        if (term == null) {
            throw new IllegalArgumentException("Subject must have a term assigned (TERM_1 or TERM_2)");
        }
        
       
        long currentTermCount = getSubjectCountByTerm(term);
        
        if (currentTermCount >= SUBJECTS_PER_TERM) {
            throw new IllegalArgumentException(
                String.format("Cannot add more than %d subjects to %s. Current: %d subjects",
                    SUBJECTS_PER_TERM, term.getDisplayName(), currentTermCount)
            );
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
    
 
    public List<Subject> getSubjectsByTerm(Term term) {
        return subjects.stream()
                .filter(subject -> subject.getTerm() == term)
                .toList();
    }
    
  
    public long getSubjectCountByTerm(Term term) {
        return subjects.stream()
                .filter(subject -> subject.getTerm() == term)
                .count();
    }
    

    public List<Subject> getTerm1Subjects() {
        return getSubjectsByTerm(Term.TERM_1);
    }
    

    public List<Subject> getTerm2Subjects() {
        return getSubjectsByTerm(Term.TERM_2);
    }
    
  
    public long getTerm1SubjectCount() {
        return getSubjectCountByTerm(Term.TERM_1);
    }

    public long getTerm2SubjectCount() {
        return getSubjectCountByTerm(Term.TERM_2);
    }
    
  
    public boolean isTermFull(Term term) {
        return getSubjectCountByTerm(term) >= SUBJECTS_PER_TERM;
    }

    public int getRemainingSlotsForTerm(Term term) {
        return (int) Math.max(0, SUBJECTS_PER_TERM - getSubjectCountByTerm(term));
    }
    
 
    public boolean isComplete() {
        return getTerm1SubjectCount() == SUBJECTS_PER_TERM && 
               getTerm2SubjectCount() == SUBJECTS_PER_TERM;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        if (students == null) {
            students = new ArrayList<>();
        }
        if (!students.contains(student)) {
            students.add(student);
            student.setLevel(this);
        }
    }

    public void removeStudent(Student student) {
        if (students != null && students.remove(student)) {
            student.setLevel(null);
        }
    }
}