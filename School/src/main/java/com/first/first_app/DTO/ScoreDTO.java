package com.first.first_app.DTO;


public class ScoreDTO {
    private int id;
    private int score;
    private boolean isTaken;
    private Integer assessmentId;
    private String assessmentTitle;
    private String assessmentType;
    private Integer subjectId;
    private String subjectName;
    private Integer studentId;
    private String studentName;
    
    public ScoreDTO() {}
    
    public ScoreDTO(com.first.first_app.Model.Score score) {
        if (score == null) return;
        
        this.id = score.getId();
        this.score = score.getScore();
        this.isTaken = score.isTaken();
        
        if (score.getAssessment() != null) {
            this.assessmentId = score.getAssessment().getAssessmentId();
            this.assessmentTitle = score.getAssessment().getTitle();
            this.assessmentType = score.getAssessment().getType() != null ? 
                score.getAssessment().getType().name() : null;
        }
        
        if (score.getSubject() != null) {
            this.subjectId = score.getSubject().getId();
            this.subjectName = score.getSubject().getName();
        }
        
        if (score.getStudent() != null) {
            this.studentId = score.getStudent().getId();
            this.studentName = score.getStudent().getName();
        }
    }
    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public boolean isTaken() { return isTaken; }
    public void setTaken(boolean taken) { isTaken = taken; }
    
    public Integer getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Integer assessmentId) { this.assessmentId = assessmentId; }
    
    public String getAssessmentTitle() { return assessmentTitle; }
    public void setAssessmentTitle(String assessmentTitle) { this.assessmentTitle = assessmentTitle; }
    
    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }
    
    public Integer getSubjectId() { return subjectId; }
    public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}