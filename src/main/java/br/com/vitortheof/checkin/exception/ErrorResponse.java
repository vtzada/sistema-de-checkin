package br.com.vitortheof.checkin.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(LocalDateTime timestamp,
                            Integer status,
                            String error,
                            String message,
                            String path) {
}
