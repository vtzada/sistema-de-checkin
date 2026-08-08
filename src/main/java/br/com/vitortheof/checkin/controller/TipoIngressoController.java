package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.TipoIngressoRequest;
import br.com.vitortheof.checkin.dto.response.TipoIngressoResponse;
import br.com.vitortheof.checkin.model.TipoIngresso;
import br.com.vitortheof.checkin.service.TipoIngressoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TipoIngressoController {

    private final TipoIngressoService tipoIngressoService;

    @PostMapping("/evento/{eventoId}/tipo-ingresso")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOwnerOrAdminOfEvento(#eventoId, authentication.principal)")
    public ResponseEntity<TipoIngressoResponse> create(@PathVariable long eventoId,
                                                       @Valid @RequestBody TipoIngressoRequest request) {
        TipoIngressoResponse response = tipoIngressoService.createTipoIngresso(eventoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/evento/{eventoId}/tipos-ingresso")
    public ResponseEntity<List<TipoIngressoResponse>> findByEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(tipoIngressoService.findByEventoId(eventoId));
    }

    @GetMapping("/tipo-ingresso/{id}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOwnerOrAdminOfTipoIngresso(#eventoId, authentication.principal)")
    public ResponseEntity<TipoIngressoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tipoIngressoService.findById(id));
    }
}
