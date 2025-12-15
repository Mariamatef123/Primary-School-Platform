package com.first.first_app.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {

    public Admin() {
        super();
        this.role = Role.ADMIN;
    }

}