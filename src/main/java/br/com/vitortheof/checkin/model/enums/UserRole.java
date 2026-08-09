package br.com.vitortheof.checkin.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("admin"),
    PRODUTOR("produtor"),
    PORTARIA("portaria"),
    CLIENTE("cliente");

    private final String role;

}
