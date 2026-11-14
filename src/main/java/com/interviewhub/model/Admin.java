package com.interviewhub.model;

import java.sql.Timestamp;

/**
 * Admin Model (extends User)
 * Represents system administrator with full platform governance privileges.
 */
public class Admin extends User {

    public Admin() {
        super();
        setRole("ADMIN");
    }

    public Admin(int userId, String name, String email, String passwordHash, String passwordSalt,
                 String status, Timestamp createdAt) {
        super(userId, name, email, passwordHash, passwordSalt, "ADMIN", status, createdAt);
    }

    @Override
    public String getRoleDetails() {
        return "Role: System Administrator | Privileges: Full Management & Analytics Access";
    }
}
