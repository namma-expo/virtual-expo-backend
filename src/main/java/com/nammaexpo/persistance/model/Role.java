package com.nammaexpo.persistance.model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "role"))
public class Role {
    @Id
    @GeneratedValue
    private int role_id;

    @Column(unique = true)
    private String role;

    public Role(){}

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
