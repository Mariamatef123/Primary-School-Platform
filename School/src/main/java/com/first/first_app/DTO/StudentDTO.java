package com.first.first_app.DTO;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

public class StudentDTO {

    private Integer id; // Optional, used for edit

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Level ID is required")
    private Integer levelId;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "SSN is required")
    private String ssn;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String password; // Optional on edit

    private String role; // usually "STUDENT"

    private List<Integer> parentIds;   // Optional list of parent IDs

    private List<PhoneDTO> phones;     // Optional list of phones

    private Integer adminId;           // Optional admin reference

    private List<Integer> teacherIds;  // Optional list of teacher IDs

    // Constructors
    public StudentDTO() {}

    public StudentDTO(Integer id, String name, Integer levelId, LocalDate dateOfBirth, String ssn,
                      String email, String password, String role, List<Integer> parentIds,
                      List<PhoneDTO> phones, Integer adminId, List<Integer> teacherIds) {
        this.id = id;
        this.name = name;
        this.levelId = levelId;
        this.dateOfBirth = dateOfBirth;
        this.ssn = ssn;
        this.email = email;
        this.password = password;
        this.role = role;
        this.parentIds = parentIds;
        this.phones = phones;
        this.adminId = adminId;
        this.teacherIds = teacherIds;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getLevelId() { return levelId; }
    public void setLevelId(Integer levelId) { this.levelId = levelId; }

    public LocalDate getdateOfBirth() { return dateOfBirth; }
    public void setdateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Integer> getParentIds() { return parentIds; }
    public void setParentIds(List<Integer> parentIds) { this.parentIds = parentIds; }

    public List<PhoneDTO> getPhones() { return phones; }
    public void setPhones(List<PhoneDTO> phones) { this.phones = phones; }

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public List<Integer> getTeacherIds() { return teacherIds; }
    public void setTeacherIds(List<Integer> teacherIds) { this.teacherIds = teacherIds; }


}
