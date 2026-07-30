package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.PatrocinadorRequest;
import br.com.vitortheof.checkin.dto.response.PatrocinadorResponse;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.service.PatrocinadorService;
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
public class PatrocinadorController {

    private final PatrocinadorService patrocinadorService;

    @PostMapping("/{eventoId}/patrocinador")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfEvento(#eventoId, authentication.principal)")
    public ResponseEntity<PatrocinadorResponse> createPatrocinador(
            @PathVariable Long eventoId,
            @Valid @RequestBody PatrocinadorRequest request,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        PatrocinadorResponse response = patrocinadorService.createPatrocinador(eventoId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/patrocinador/{id}")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfPatrocinador(#id, authentication.principal)")
    public ResponseEntity<PatrocinadorResponse> findById(
            @PathVariable Long id) {
        PatrocinadorResponse response = patrocinadorService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventoId}/patrocinadores")
    public ResponseEntity<List<PatrocinadorResponse>> findAllByEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(patrocinadorService.findByEventoId(eventoId));
    }

}
