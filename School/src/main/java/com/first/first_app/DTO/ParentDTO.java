package com.first.first_app.DTO;

import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import java.util.List;
import java.util.stream.Collectors;

public class ParentDTO {
    
  
    private int id;
    private String name;
    private String email;
    private String role;
    private String password;
    

    private List<ChildSimpleDTO> children;
    private int totalChildren;
    
 
    private Integer familyId;
    private String familyName;
    
  
    private List<PhoneDTO> phones;
    

    private int totalChildrenActive;
    private double averageChildrenProgress;

    public ParentDTO() {}
    
    public ParentDTO(Parent parent) {
        if (parent == null) return;
        
        System.out.println("=== Building ParentDTO ===");
        System.out.println("Parent ID: " + parent.getId());
        System.out.println("Parent phones: " + parent.getPhones());
        System.out.println("Phones size: " + (parent.getPhones() != null ? parent.getPhones().size() : 0));
        
  
        this.id = parent.getId();
        this.name = parent.getName();
        this.email = parent.getEmail();
        this.role = parent.getRole() != null ? parent.getRole().name() : "PARENT";
        
  
        if (parent.getPhones() != null && !parent.getPhones().isEmpty()) {
            System.out.println("Mapping " + parent.getPhones().size() + " phones");
            this.phones = parent.getPhones().stream()
                    .map(phone -> {
                        System.out.println("Phone: " + phone.getPhoneNumber() + " - " + phone.getPhoneType());
                        return new PhoneDTO(phone.getPhoneNumber(), phone.getPhoneType());
                    })
                    .collect(Collectors.toList());
        } else {
            System.out.println("No phones found in parent entity");
            this.phones = null;
        }
        
 
        if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
            System.out.println("Mapping " + parent.getChildren().size() + " children");
            this.children = parent.getChildren().stream()
                    .map(ChildSimpleDTO::new)
                    .collect(Collectors.toList());
            this.totalChildren = this.children.size();
            
    
            this.totalChildrenActive = (int) parent.getChildren().stream()
                    .filter(child -> child.getStatus() != null && 
                           (child.getStatus().name().equals("ACTIVE") || 
                            child.getStatus().name().equals("PASSED")))
                    .count();
            
            this.averageChildrenProgress = parent.getChildren().stream()
                    .mapToDouble(child -> {
                        if (child.getStatus() != null) {
                            switch (child.getStatus().name()) {
                                case "PASSED": return 100.0;
                                case "ACTIVE": return 75.0;
                                case "SUMMER_EXAM": return 50.0;
                                case "FAILED": return 25.0;
                                default: return 0.0;
                            }
                        }
                        return 0.0;
                    })
                    .average()
                    .orElse(0.0);
        } else {
            System.out.println("No children found in parent entity");
            this.children = null;
            this.totalChildren = 0;
            this.totalChildrenActive = 0;
            this.averageChildrenProgress = 0.0;
        }
        
  
        if (parent.getFamily() != null) {
            this.familyId = parent.getFamily().getId();
            this.familyName = parent.getFamily().getFamilyName();
            System.out.println("Family: " + this.familyName);
        } else {
            this.familyId = null;
            this.familyName = null;
            System.out.println("No family found");
        }
        
        System.out.println("=== ParentDTO Built Successfully ===");
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
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
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getRole() { 
        return role; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }
    
    public List<ChildSimpleDTO> getChildren() { 
        return children; 
    }
    
    public void setChildren(List<ChildSimpleDTO> children) { 
        this.children = children; 
    }
    
    public int getTotalChildren() { 
        return totalChildren; 
    }
    
    public void setTotalChildren(int totalChildren) { 
        this.totalChildren = totalChildren; 
    }
    
    public Integer getFamilyId() { 
        return familyId; 
    }
    
    public void setFamilyId(Integer familyId) { 
        this.familyId = familyId; 
    }
    
    public String getFamilyName() { 
        return familyName; 
    }
    
    public void setFamilyName(String familyName) { 
        this.familyName = familyName; 
    }
    
    public List<PhoneDTO> getPhones() { 
        return phones; 
    }
    
    public void setPhones(List<PhoneDTO> phones) { 
        this.phones = phones; 
    }
    
    public int getTotalChildrenActive() { 
        return totalChildrenActive; 
    }
    
    public void setTotalChildrenActive(int totalChildrenActive) { 
        this.totalChildrenActive = totalChildrenActive; 
    }
    
    public double getAverageChildrenProgress() { 
        return averageChildrenProgress; 
    }
    
    public void setAverageChildrenProgress(double averageChildrenProgress) { 
        this.averageChildrenProgress = averageChildrenProgress; 
    }
    
    
    public static class ChildSimpleDTO {
        private int id;
        private String name;
        private String email;
        private String levelName;
        private String status;
        private String currentTerm;
        private double averageScore;
        private int totalAssessmentsCompleted;
        
        public ChildSimpleDTO(Student student) {
            if (student == null) return;
            
            this.id = student.getId();
            this.name = student.getName();
            this.email = student.getEmail();
            
            if (student.getLevel() != null) {
                this.levelName = student.getLevel().getName();
            }
            
            this.status = student.getStatus() != null ? student.getStatus().name() : "PENDING";
            this.currentTerm = student.getCurrentTerm() != null ? student.getCurrentTerm().name() : "TERM_1";
            
            if (student.getScores() != null && !student.getScores().isEmpty()) {
                this.averageScore = student.getScores().stream()
                        .mapToDouble(Score::getScore)
                        .average()
                        .orElse(0.0);
                this.totalAssessmentsCompleted = student.getScores().size();
            }
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
        
        public String getCurrentTerm() { return currentTerm; }
        public void setCurrentTerm(String currentTerm) { this.currentTerm = currentTerm; }
        
        public double getAverageScore() { return averageScore; }
        public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
        
        public int getTotalAssessmentsCompleted() { return totalAssessmentsCompleted; }
        public void setTotalAssessmentsCompleted(int totalAssessmentsCompleted) { 
            this.totalAssessmentsCompleted = totalAssessmentsCompleted; 
        }
    }
}