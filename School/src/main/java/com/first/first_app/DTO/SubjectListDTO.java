package com.first.first_app.DTO;

import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Subject;

public class SubjectListDTO {

    private int id;
    private int code;
    private String name;
    private Term term;
    private Integer levelId;
    private String levelName;
    private Integer teacherId;
    private String teacherName;

   public SubjectListDTO(){}

    public SubjectListDTO(Subject subject) {
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


}
