package com.first.first_app.DTO;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.first.first_app.Enum.StudentStatus;
import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Teacher;

public class StudentListDTO {
    
 
    private int id;
    private String name;
    private String email;
    private String password;
    private String ssn;
    private LocalDate dateOfBirth;
    private int age;
  
    private Integer levelId;
    private String levelName;
    private Integer levelNumber;
    
    private Term currentTerm;
    

    private StudentStatus status;
    private boolean isLocked;
    private boolean isActive;
    

    private boolean isAssessmentsLocked;
    private int unlockedAssessmentCount;

    private Set<Integer> completedLevelIds = new HashSet<>();
    private Map<String, LocalDate> completedTerms = new HashMap<>();
    
    private List<ParentInfoDTO> parents;
    private List<Integer> parentIds;
    

    private List<TeacherInfoDTO> teachers;
    private List<Integer> teacherIds;
    
 
    private Integer adminId;
    private String adminName;
    

    private List<SubjectListDTO> subjects;
    private List<SubjectInfoDTO> subjectsFailed;
    private List<ScoreDTO> scores;
    private List<PhoneDTO> phones;
    
   
    private double averageScore;
    private int totalAssignmentsCompleted;
    private int totalExamsCompleted;
    private double term1Progress;
    private double term2Progress;

    public StudentListDTO() {}
    
    public StudentListDTO(Student student) {
        if (student == null) return;
        
  
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.ssn = student.getSsn();
        this.dateOfBirth = student.getDateOfBirth();
        this.currentTerm = student.getCurrentTerm();
        this.status = student.getStatus();
        this.isLocked = student.getLocked();
        this.isAssessmentsLocked = student.getAssessmentsLocked();
        this.unlockedAssessmentCount = student.getUnlockedAssessmentCount();
        this.completedLevelIds = student.getCompletedLevelIds() != null ? 
                student.getCompletedLevelIds() : new HashSet<>();
        this.completedTerms = student.getCompletedTerms() != null ? 
                student.getCompletedTerms() : new HashMap<>();
        this.isActive = student.getStatus() == StudentStatus.PASSED || 
                        student.getStatus() == StudentStatus.PENDING;
        this.password = student.getPassword();
        
        if (student.getDateOfBirth() != null) {
            this.age = Period.between(student.getDateOfBirth(), LocalDate.now()).getYears();
        }
        

        if (student.getLevel() != null) {
            this.levelId = student.getLevel().getId();
            this.levelName = student.getLevel().getName();
            String name = student.getLevel().getName();
            if (name != null && name.matches(".*\\d+.*")) {
                this.levelNumber = Integer.parseInt(name.replaceAll("\\D+", ""));
            }
        }
        
       
        if (student.getAdmin() != null) {
            this.adminId = student.getAdmin().getId();
            this.adminName = student.getAdmin().getName();
        }
        
     
        if (student.getParents() != null && !student.getParents().isEmpty()) {
            this.parents = student.getParents().stream()
                    .map(ParentInfoDTO::new)
                    .collect(Collectors.toList());
            this.parentIds = student.getParents().stream()
                    .map(Parent::getId)
                    .collect(Collectors.toList());
        }
        
    
        if (student.getTeachers() != null && !student.getTeachers().isEmpty()) {
            this.teachers = student.getTeachers().stream()
                    .map(TeacherInfoDTO::new)
                    .collect(Collectors.toList());
            this.teacherIds = student.getTeachers().stream()
                    .map(Teacher::getId)
                    .collect(Collectors.toList());
        }
   
        if (student.getSubjects() != null && !student.getSubjects().isEmpty()) {
            this.subjects = student.getSubjects().stream()
                    .map(SubjectListDTO::new)
                    .collect(Collectors.toList());
        }
        
        if (student.getSubjectsFailed() != null && !student.getSubjectsFailed().isEmpty()) {
            this.subjectsFailed = student.getSubjectsFailed().stream()
                    .map(SubjectInfoDTO::new)
                    .collect(Collectors.toList());
        }
        
      
        if (student.getScores() != null && !student.getScores().isEmpty()) {
            this.scores = student.getScores().stream()
                    .map(ScoreDTO::new)
                    .collect(Collectors.toList());
            
          
            this.totalAssignmentsCompleted = (int) student.getScores().stream()
                    .filter(s -> s.getAssessment() != null && "ASSIGNMENT".equals(s.getAssessment().getType()))
                    .count();
            this.totalExamsCompleted = (int) student.getScores().stream()
                    .filter(s -> s.getAssessment() != null && "EXAM".equals(s.getAssessment().getType()))
                    .count();
            
            this.averageScore = student.getScores().stream()
                    .mapToDouble(Score::getScore)
                    .average()
                    .orElse(0.0);
        }
        
     
        if (student.getPhones() != null && !student.getPhones().isEmpty()) {
            this.phones = student.getPhones().stream()
                    .map(phone -> new PhoneDTO(phone.getPhoneNumber(), phone.getPhoneType()))
                    .collect(Collectors.toList());
        }
    }
    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public Integer getLevelId() { return levelId; }
    public void setLevelId(Integer levelId) { this.levelId = levelId; }
    
    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    
    public Integer getLevelNumber() { return levelNumber; }
    public void setLevelNumber(Integer levelNumber) { this.levelNumber = levelNumber; }
    
    public Term getCurrentTerm() { return currentTerm; }
    public void setCurrentTerm(Term currentTerm) { this.currentTerm = currentTerm; }
    
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
    
    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isAssessmentsLocked() { return isAssessmentsLocked; }
    public void setAssessmentsLocked(boolean assessmentsLocked) { isAssessmentsLocked = assessmentsLocked; }
    
    public int getUnlockedAssessmentCount() { return unlockedAssessmentCount; }
    public void setUnlockedAssessmentCount(int unlockedAssessmentCount) { this.unlockedAssessmentCount = unlockedAssessmentCount; }
    
    public Set<Integer> getCompletedLevelIds() { return completedLevelIds; }
    public void setCompletedLevelIds(Set<Integer> completedLevelIds) { this.completedLevelIds = completedLevelIds; }
    
    public Map<String, LocalDate> getCompletedTerms() { return completedTerms; }
    public void setCompletedTerms(Map<String, LocalDate> completedTerms) { this.completedTerms = completedTerms; }
    
    public List<ParentInfoDTO> getParents() { return parents; }
    public void setParents(List<ParentInfoDTO> parents) { this.parents = parents; }
    
    public List<Integer> getParentIds() { return parentIds; }
    public void setParentIds(List<Integer> parentIds) { this.parentIds = parentIds; }
    
    public List<TeacherInfoDTO> getTeachers() { return teachers; }
    public void setTeachers(List<TeacherInfoDTO> teachers) { this.teachers = teachers; }
    
    public List<Integer> getTeacherIds() { return teacherIds; }
    public void setTeacherIds(List<Integer> teacherIds) { this.teacherIds = teacherIds; }
    
    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }
    
    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
    
    public List<SubjectListDTO> getSubjects() { return subjects; }
    public void setSubjects(List<SubjectListDTO> subjects) { this.subjects = subjects; }
    
    public List<SubjectInfoDTO> getSubjectsFailed() { return subjectsFailed; }
    public void setSubjectsFailed(List<SubjectInfoDTO> subjectsFailed) { this.subjectsFailed = subjectsFailed; }
    
    public List<ScoreDTO> getScores() { return scores; }
    public void setScores(List<ScoreDTO> scores) { this.scores = scores; }
    
    public List<PhoneDTO> getPhones() { return phones; }
    public void setPhones(List<PhoneDTO> phones) { this.phones = phones; }
    
    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
    
    public int getTotalAssignmentsCompleted() { return totalAssignmentsCompleted; }
    public void setTotalAssignmentsCompleted(int totalAssignmentsCompleted) { this.totalAssignmentsCompleted = totalAssignmentsCompleted; }
    
    public int getTotalExamsCompleted() { return totalExamsCompleted; }
    public void setTotalExamsCompleted(int totalExamsCompleted) { this.totalExamsCompleted = totalExamsCompleted; }
    
    public double getTerm1Progress() { return term1Progress; }
    public void setTerm1Progress(double term1Progress) { this.term1Progress = term1Progress; }
    
    public double getTerm2Progress() { return term2Progress; }
    public void setTerm2Progress(double term2Progress) { this.term2Progress = term2Progress; }
    
    public static class ParentInfoDTO {
        private int id;
        private String name;
        private String email;
        
        public ParentInfoDTO(Parent parent) {
            this.id = parent.getId();
            this.name = parent.getName();
            this.email = parent.getEmail();
        }
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    public static class TeacherInfoDTO {
        private int id;
        private String name;
        private String email;
        
        public TeacherInfoDTO(Teacher teacher) {
            this.id = teacher.getId();
            this.name = teacher.getName();
            this.email = teacher.getEmail();
        }
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    public static class SubjectInfoDTO {
        private int id;
        private int code;
        private String name;
        private String term;
        
        public SubjectInfoDTO(Subject subject) {
            this.id = subject.getId();
            this.code = subject.getCode();
            this.name = subject.getName();
            this.term = subject.getTerm() != null ? subject.getTerm().name() : null;
        }
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getCode() { return code; }
        public void setCode(int code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getTerm() { return term; }
        public void setTerm(String term) { this.term = term; }
    }
}