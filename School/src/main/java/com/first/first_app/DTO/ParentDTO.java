package com.first.first_app.DTO;

import java.util.List;

public class ParentDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private List<StudentDTO> children;
    private List<PhoneDTO> phones;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<StudentDTO> getChildren() { return children; }
    public void setChildren(List<StudentDTO> children) { this.children = children; }
    public List<PhoneDTO> getPhones() { return phones; }
    public void setPhones(List<PhoneDTO> phones) { this.phones = phones; }
}