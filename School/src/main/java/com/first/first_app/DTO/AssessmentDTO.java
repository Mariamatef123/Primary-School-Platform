package com.first.first_app.DTO;

import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Question;

import java.util.List;
import java.util.stream.Collectors;

public class AssessmentDTO {
    private int assessmentId;
    private String title;
    private String type;
    private int numOfQues;
    private Integer duration;
    private boolean summerExam;
    private Integer subjectId;
    private String subjectName;
    private List<Question> questions;  
    private ScoreDTO score;  
    
    public AssessmentDTO() {}
    
    public AssessmentDTO(Assessment assessment) {
        if (assessment == null) return;
        
        this.assessmentId = assessment.getAssessmentId();
        this.title = assessment.getTitle();
        this.type = assessment.getType() != null ? assessment.getType().name() : "EXAM";
        
        this.numOfQues = assessment.getNumOfQues() ;
        this.duration = assessment.getDuration();
        this.summerExam = assessment.getSummerExam();
        
        if (assessment.getSubject() != null) {
            this.subjectId = assessment.getSubject().getId();
            this.subjectName = assessment.getSubject().getName();
        }
        
   
        if (assessment.getQuestions() != null && !assessment.getQuestions().isEmpty()) {
            this.questions = assessment.getQuestions();
        }
    }
    

    public AssessmentDTO(Assessment assessment, Integer studentId) {
        this(assessment);
        
        if (studentId != null && assessment.getScores() != null) {
            this.score = assessment.getScores().stream()
                    .filter(s -> s.getStudent() != null && s.getStudent().getId() == studentId)
                    .findFirst()
                    .map(ScoreDTO::new)
                    .orElse(null);
        }
    }
    

    public int getAssessmentId() { return assessmentId; }
    public void setAssessmentId(int assessmentId) { this.assessmentId = assessmentId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getNumOfQues() { return numOfQues; }
    public void setNumOfQues(int numOfQues) { this.numOfQues = numOfQues; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    public boolean isSummerExam() { return summerExam; }
    public void setSummerExam(boolean summerExam) { this.summerExam = summerExam; }
    
    public Integer getSubjectId() { return subjectId; }
    public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    
    public ScoreDTO getScore() { return score; }
    public void setScore(ScoreDTO score) { this.score = score; }
}