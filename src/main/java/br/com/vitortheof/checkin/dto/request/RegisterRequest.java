package br.com.vitortheof.checkin.dto.request;

import br.com.vitortheof.checkin.model.enums.UserRole;

public record RegisterRequest(String nome, String email, String senha, UserRole role) {
}
