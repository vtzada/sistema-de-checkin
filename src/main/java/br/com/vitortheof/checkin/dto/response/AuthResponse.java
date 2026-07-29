package br.com.vitortheof.checkin.dto.response;

import br.com.vitortheof.checkin.model.enums.UserRole;

public record AuthResponse(String token,
                           Long id,
                           String nome,
                           String email,
                           UserRole role) {
}
