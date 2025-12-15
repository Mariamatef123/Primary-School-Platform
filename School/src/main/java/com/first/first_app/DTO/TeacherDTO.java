package com.first.first_app.DTO;

import java.util.List;

public class TeacherDTO {

    private String name;
    private String email;
    private String password;
    private String role;
    private Integer subjectId;
    private Integer adminId; // Add adminId to DTO
    private List<PhoneDTO> phones;

    public TeacherDTO() {}

    public TeacherDTO(String name, String email, String password, String role,
                      Integer subjectId, Integer adminId, List<PhoneDTO> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.subjectId = subjectId;
        this.adminId = adminId;
        this.phones = phones;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getSubjectId() { return subjectId; }
    public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public List<PhoneDTO> getPhones() { return phones; }
    public void setPhones(List<PhoneDTO> phones) { this.phones = phones; }

    // Nested PhoneDTO
    public static class PhoneDTO {
        private String phoneNumber;
        private String phoneType;

        public PhoneDTO() {}

        public PhoneDTO(String phoneNumber, String phoneType) {
            this.phoneNumber = phoneNumber;
            this.phoneType = phoneType;
        }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getPhoneType() { return phoneType; }
        public void setPhoneType(String phoneType) { this.phoneType = phoneType; }
    }
}
