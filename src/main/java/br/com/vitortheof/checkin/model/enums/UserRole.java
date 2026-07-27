package br.com.vitortheof.checkin.model.enums;

public enum UserRole {
    ADMIN("admin"),
    PRODUTOR("produtor"),
    PORTARIA("portaria");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
