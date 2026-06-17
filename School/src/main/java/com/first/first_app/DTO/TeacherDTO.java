package com.first.first_app.DTO;

import com.first.first_app.Model.*;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherDTO {
    

    private int id;
    private String name;
    private String email;
    private String role;
    private String password;
  
    private Integer subjectId;
    private String subjectName;
    private Integer subjectCode;
    private String subjectTerm;
    
    private List<StudentSimpleDTO> students;
    private int totalStudents;

    private List<Score> scores;
    private double averageScore;
    private int totalScores;
    
   
    private List<Assessment> assessments;
    private int totalAssessments;
    

    private List<PhoneDTO> phones;
    
   
    private int totalAssessmentsCreated;
    private double totalPointsGiven;
    
    
    public TeacherDTO() {}
    
    public TeacherDTO(Teacher teacher) {
        if (teacher == null) return;
        
      
        this.id = teacher.getId();
        this.name = teacher.getName();
        this.email = teacher.getEmail();
        this.role = teacher.getRole() != null ? teacher.getRole().name() : "TEACHER";
        
      
        if (teacher.getSubject() != null) {
            this.subjectId = teacher.getSubject().getId();
            this.subjectName = teacher.getSubject().getName();
            this.subjectCode = teacher.getSubject().getCode();
            if (teacher.getSubject().getTerm() != null) {
                this.subjectTerm = teacher.getSubject().getTerm().name();
            }
        }
        

        if (teacher.getStudents() != null && !teacher.getStudents().isEmpty()) {
            this.students = teacher.getStudents().stream()
                    .map(StudentSimpleDTO::new)
                    .collect(Collectors.toList());
            this.totalStudents = this.students.size();
        }
        

        if (teacher.getPhones() != null && !teacher.getPhones().isEmpty()) {
            this.phones = teacher.getPhones().stream()
                    .map(phone -> new PhoneDTO(phone.getPhoneNumber(), phone.getPhoneType()))
                    .collect(Collectors.toList());
        }
    }
    
 
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
    public List<Score> getScores() {
        return scores;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Integer getSubjectId() { return subjectId; }
    public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public Integer getSubjectCode() { return subjectCode; }
    public void setSubjectCode(Integer subjectCode) { this.subjectCode = subjectCode; }
    
    public String getSubjectTerm() { return subjectTerm; }
    public void setSubjectTerm(String subjectTerm) { this.subjectTerm = subjectTerm; }
    
    public List<StudentSimpleDTO> getStudents() { return students; }
    public void setStudents(List<StudentSimpleDTO> students) { this.students = students; }
    
    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
    

    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
    
    public int getTotalScores() { return totalScores; }
    public void setTotalScores(int totalScores) { this.totalScores = totalScores; }
    
    public List<Assessment> getAssessments() { return assessments; }
    public void setAssessments(List<Assessment> assessments) { this.assessments = assessments; }
    
    public int getTotalAssessments() { return totalAssessments; }
    public void setTotalAssessments(int totalAssessments) { this.totalAssessments = totalAssessments; }
    
    public List<PhoneDTO> getPhones() { return phones; }
    public void setPhones(List<PhoneDTO> phones) { this.phones = phones; }
    
    public int getTotalAssessmentsCreated() { return totalAssessmentsCreated; }
    public void setTotalAssessmentsCreated(int totalAssessmentsCreated) { this.totalAssessmentsCreated = totalAssessmentsCreated; }
    
    public double getTotalPointsGiven() { return totalPointsGiven; }
    public void setTotalPointsGiven(double totalPointsGiven) { this.totalPointsGiven = totalPointsGiven; }
    

    public static class StudentSimpleDTO {
        private int id;
        private String name;
        private String email;
        private String levelName;
        private String status;
        
        public StudentSimpleDTO(Student student) {
            this.id = student.getId();
            this.name = student.getName();
            this.email = student.getEmail();
            if (student.getLevel() != null) {
                this.levelName = student.getLevel().getName();
            }
            this.status = student.getStatus() != null ? student.getStatus().name() : "PENDING";
        }
        
       
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getLevelName() { return levelName; }
        public void setLevelName(String levelName) { this.levelName = levelName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
   


}