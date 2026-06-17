package com.first.first_app.DTO;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.first.first_app.Model.Family;
import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;

public class FamilyResponseDTO {
    
    private int familyId;
    private String familyName;
    private List<ParentInfoDTO> parents;
    private List<ChildInfoDTO> children;
    
   
    private int totalParents;
    private int totalChildren;
    
    public FamilyResponseDTO() {}
    
    public FamilyResponseDTO(Family family) {
        if (family == null) return;
        
        this.familyId = family.getId();
        this.familyName = family.getFamilyName();
  
        List<ParentInfoDTO> parentDTOs = new ArrayList<>();
        Map<Integer, Student> uniqueChildren = new LinkedHashMap<>();
        
        if (family.getParents() != null && !family.getParents().isEmpty()) {
            for (Parent parent : family.getParents()) {
            
                parentDTOs.add(new ParentInfoDTO(parent));
                
              
                if (parent.getChildren() != null) {
                    for (Student child : parent.getChildren()) {
                        uniqueChildren.putIfAbsent(child.getId(), child);
                    }
                }
            }
        }
        
        this.parents = parentDTOs;
        this.totalParents = parentDTOs.size();
        
    
        List<ChildInfoDTO> childDTOs = uniqueChildren.values().stream()
                .map(ChildInfoDTO::new)
                .collect(Collectors.toList());
        this.children = childDTOs;
        this.totalChildren = childDTOs.size();
    }
    

    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }
    
    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }
    
    public List<ParentInfoDTO> getParents() { return parents; }
    public void setParents(List<ParentInfoDTO> parents) { this.parents = parents; }
    
    public List<ChildInfoDTO> getChildren() { return children; }
    public void setChildren(List<ChildInfoDTO> children) { this.children = children; }
    
    public int getTotalParents() { return totalParents; }
    public void setTotalParents(int totalParents) { this.totalParents = totalParents; }
    
    public int getTotalChildren() { return totalChildren; }
    public void setTotalChildren(int totalChildren) { this.totalChildren = totalChildren; }
    

    public static class ParentInfoDTO {
        private int id;
        private String name;
        private String email;
        private List<PhoneInfoDTO> phones;
        private int numberOfChildren;
        
        public ParentInfoDTO(Parent parent) {
            this.id = parent.getId();
            this.name = parent.getName();
            this.email = parent.getEmail();
            
            if (parent.getPhones() != null && !parent.getPhones().isEmpty()) {
                this.phones = parent.getPhones().stream()
                        .map(phone -> new PhoneInfoDTO(phone.getPhoneNumber(), phone.getPhoneType()))
                        .collect(Collectors.toList());
            }
            
            this.numberOfChildren = parent.getChildren() != null ? parent.getChildren().size() : 0;
        }
        
    
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public List<PhoneInfoDTO> getPhones() { return phones; }
        public void setPhones(List<PhoneInfoDTO> phones) { this.phones = phones; }
        
        public int getNumberOfChildren() { return numberOfChildren; }
        public void setNumberOfChildren(int numberOfChildren) { this.numberOfChildren = numberOfChildren; }
    }
    
    public static class ChildInfoDTO {
        private int id;
        private String name;
        private String email;
        private String levelName;
        private String status;
        private String currentTerm;
        private double averageScore;
        private List<ParentReferenceDTO> parents;
        
        public ChildInfoDTO(Student child) {
            this.id = child.getId();
            this.name = child.getName();
            this.email = child.getEmail();
            
            if (child.getLevel() != null) {
                this.levelName = child.getLevel().getName();
            }
            
            this.status = child.getStatus() != null ? child.getStatus().name() : "PENDING";
            this.currentTerm = child.getCurrentTerm() != null ? child.getCurrentTerm().name() : "TERM_1";
   
            if (child.getScores() != null && !child.getScores().isEmpty()) {
                this.averageScore = child.getScores().stream()
                        .mapToDouble(Score::getScore)
                        .average()
                        .orElse(0.0);
            }
            
         
            if (child.getParents() != null && !child.getParents().isEmpty()) {
                this.parents = child.getParents().stream()
                        .map(ParentReferenceDTO::new)
                        .collect(Collectors.toList());
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
        
        public List<ParentReferenceDTO> getParents() { return parents; }
        public void setParents(List<ParentReferenceDTO> parents) { this.parents = parents; }
    }
    
    public static class ParentReferenceDTO {
        private int id;
        private String name;
        private String email;
        
        public ParentReferenceDTO(Parent parent) {
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
    
    public static class PhoneInfoDTO {
        private String phoneNumber;
        private String phoneType;
        
        public PhoneInfoDTO(String phoneNumber, String phoneType) {
            this.phoneNumber = phoneNumber;
            this.phoneType = phoneType;
        }
        
       
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getPhoneType() { return phoneType; }
        public void setPhoneType(String phoneType) { this.phoneType = phoneType; }
    }
}