package br.com.vitortheof.checkin.controller;


import br.com.vitortheof.checkin.dto.request.PacoteRequest;
import br.com.vitortheof.checkin.dto.response.PacoteResponse;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.service.PacoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento")
@RequiredArgsConstructor
public class PacoteController {

    private final PacoteService pacoteService;

    @PostMapping("/{eventoId}/pacote")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfEvento(#eventoId, authentication.principal)")
    public ResponseEntity<PacoteResponse> createPacote(
            @PathVariable Long eventoId,
            @Valid  @RequestBody PacoteRequest pacoteRequest,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        PacoteResponse response = pacoteService.createPacote(eventoId, pacoteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pacote/{id}")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfPacote(#id, authentication.principal)")
    public ResponseEntity<PacoteResponse> findById(@PathVariable Long id) {
        PacoteResponse response = pacoteService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{eventoId}/pacotes")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfEvento(#eventoId, authentication.principal)")
    public ResponseEntity<List<PacoteResponse>> findAllByEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(pacoteService.findByEventoId(eventoId));
    }
}
