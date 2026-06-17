package com.first.first_app.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.first_app.Enum.StudentStatus;
import com.first.first_app.Enum.Term;

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
   @ManyToMany
@JoinTable(
    name = "subjects_failed",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "subject_id")
)
private List<Subject> subjectsFailed;

    private List<Integer> parentIds;

    private StudentStatus status=StudentStatus.PENDING;
   @Column(name = "is_locked", nullable = false)
   
private boolean isLocked = false;
 @Column(name = "is_assessments_locked",nullable = false)
private boolean isAssessmentsLocked=false;
 @Column(nullable = false)
    private int unlockedAssessmentCount = 0;
    

    public int getUnlockedAssessmentCount() {
        return unlockedAssessmentCount;
    }
    
    public void setUnlockedAssessmentCount(int unlockedAssessmentCount) {
        this.unlockedAssessmentCount = unlockedAssessmentCount;
    }
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

    

    @Enumerated(EnumType.STRING)
    private Term currentTerm = Term.TERM_1;

     
    @ElementCollection
    @CollectionTable(name = "student_completed_levels", 
                     joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "level_id")
    private Set<Integer> completedLevelIds = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "student_completed_terms", 
                     joinColumns = @JoinColumn(name = "student_id"))
    private Map<String, LocalDate> completedTerms = new HashMap<>(); 
    
  
    public Set<Integer> getCompletedLevelIds() {
        return completedLevelIds;
    }
    
    public void setCompletedLevelIds(Set<Integer> completedLevelIds) {
        this.completedLevelIds = completedLevelIds;
    }
    
    public Map<String, LocalDate> getCompletedTerms() {
        return completedTerms;
    }
    
    public void setCompletedTerms(Map<String, LocalDate> completedTerms) {
        this.completedTerms = completedTerms;
    }
    
    public void addCompletedTerm(int levelId, Term term, LocalDate completionDate) {
        String key = "LEVEL_" + levelId + "_" + term.name();
        completedTerms.put(key, completionDate);
    }
    
    public boolean isTermCompleted(int levelId, Term term) {
        String key = "LEVEL_" + levelId + "_" + term.name();
        return completedTerms.containsKey(key);
    }
    public void setAssessmentsLocked(boolean isAssessmentsLocked) {
        this.isAssessmentsLocked = isAssessmentsLocked;
    }
    
    public void setCurrentTerm(Term currentTerm) {
        this.currentTerm = currentTerm;
    }

    public Term getCurrentTerm() {
        return currentTerm;
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

    public void setLevel(Level level2) {
        this.level = level2;
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
    public void setStatus(StudentStatus status){
        this.status=status;
    }
    public StudentStatus getStatus(){
     return status;
    }
    public void setSubjectsFailed(List<Subject> subjectsFailed) {
        this.subjectsFailed = subjectsFailed;
    }
   public List<Subject>getSubjectsFailed(){
    return subjectsFailed;
   }
   public void addSubjectFailed(Subject subject){
    this.subjectsFailed.add(subject);
   }
   public void setLocked(boolean isLocked) {
       this.isLocked = isLocked;
   }
   public boolean getLocked(){
    return isLocked;
   }
     public boolean getAssessmentsLocked(){
    return isAssessmentsLocked;
   }
   
}
