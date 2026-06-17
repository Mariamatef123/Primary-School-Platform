
package com.first.first_app.DTO;

import com.first.first_app.Model.Level;

import java.util.List;
import java.util.stream.Collectors;

public class LevelDTO {
    private int id;
    private String name;
    private long term1SubjectCount;
    private long term2SubjectCount;
    private List<SubjectListDTO> term1Subjects;
    private List<SubjectListDTO> term2Subjects;
    private int totalSubjects;
    
    public LevelDTO() {}
    
    public LevelDTO(Level level) {
        this.id = level.getId();
        this.name = level.getName();
        this.term1SubjectCount = level.getTerm1SubjectCount();
        this.term2SubjectCount = level.getTerm2SubjectCount();
        this.totalSubjects = level.getSubjects().size();
        
        this.term1Subjects = level.getTerm1Subjects().stream()
                .map(SubjectListDTO::new)
                .collect(Collectors.toList());
        
        this.term2Subjects = level.getTerm2Subjects().stream()
                .map(SubjectListDTO::new)
                .collect(Collectors.toList());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public long getTerm1SubjectCount() { return term1SubjectCount; }
    public void setTerm1SubjectCount(long term1SubjectCount) { this.term1SubjectCount = term1SubjectCount; }
    
    public long getTerm2SubjectCount() { return term2SubjectCount; }
    public void setTerm2SubjectCount(long term2SubjectCount) { this.term2SubjectCount = term2SubjectCount; }
    
    public List<SubjectListDTO> getTerm1Subjects() { return term1Subjects; }
    public void setTerm1Subjects(List<SubjectListDTO> term1Subjects) { this.term1Subjects = term1Subjects; }
    
    public List<SubjectListDTO> getTerm2Subjects() { return term2Subjects; }
    public void setTerm2Subjects(List<SubjectListDTO> term2Subjects) { this.term2Subjects = term2Subjects; }
    
    public int getTotalSubjects() { return totalSubjects; }
    public void setTotalSubjects(int totalSubjects) { this.totalSubjects = totalSubjects; }
}