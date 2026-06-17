package com.first.first_app.DTO;

import java.util.List;
import java.util.stream.Collectors;

import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Assessment;

public class SubjectDTO {
    private int id;
    private int code;
    private String name;
    private Term term;
    private Integer levelId;
    private String levelName;
    private Integer teacherId;
    private String teacherName;
    private List<AssessmentListDTO>assessments;
    
    public SubjectDTO() {}
    
    public SubjectDTO(com.first.first_app.Model.Subject subject) {
        this.id = subject.getId();
        this.code = subject.getCode();
        this.name = subject.getName();
        this.term = subject.getTerm();
        
        if (subject.getLevel() != null) {
            this.levelId = subject.getLevel().getId();
            this.levelName = subject.getLevel().getName();
        }
        
        if (subject.getTeacher() != null) {
            this.teacherId = subject.getTeacher().getId();
            this.teacherName = subject.getTeacher().getName();
        }
        if (subject.getAssessments() != null && !subject.getAssessments().isEmpty()) {
            this.assessments = subject.getAssessments().stream()
                    .filter(assessment -> assessment != null)
                    .map(AssessmentListDTO::new)
                    .collect(Collectors.toList());
        }
    }
    
  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Term getTerm() { return term; }
    public void setTerm(Term term) { this.term = term; }
    
    public Integer getLevelId() { return levelId; }
    public void setLevelId(Integer levelId) { this.levelId = levelId; }
    
    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    
    public Integer getTeacherId() { return teacherId; }
    public void setTeacherId(Integer teacherId) { this.teacherId = teacherId; }
    
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public void setAssessments(List<AssessmentListDTO> assessments) {
        this.assessments = assessments;
    }
    public List<AssessmentListDTO> getAssessments() {
        return assessments;
    }
}