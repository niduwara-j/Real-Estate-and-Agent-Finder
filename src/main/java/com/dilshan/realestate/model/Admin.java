package com.dilshan.realestate.model;

import com.dilshan.realestate.model.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    private String department;
    private boolean isSuperAdmin = false;

    public Admin() {
        super();
        setRole(Role.ADMIN);
    }

    public Admin(String name, String email, String password, String contactNumber,
                 String department, boolean isSuperAdmin) {
        super(name, email, password, contactNumber, Role.ADMIN);
        this.department = department;
        this.isSuperAdmin = isSuperAdmin;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }
}
