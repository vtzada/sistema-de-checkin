package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.ConvidadoRequest;
import br.com.vitortheof.checkin.dto.response.ConvidadoResponse;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.service.ConvidadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patrocinador")
@RequiredArgsConstructor
public class ConvidadoController {

    private final ConvidadoService convidadoService;

    @PostMapping("/{patrocinadorId}/convidado")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfPatrocinador(#patrocinadorId, authentication.principal)")
    public ResponseEntity<ConvidadoResponse> createConvidado(
            @PathVariable Long patrocinadorId,
            @Valid @RequestBody ConvidadoRequest request,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        ConvidadoResponse response = convidadoService.createConvidado(patrocinadorId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{patrocinadorId}/convidados")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfPatrocinador(#patrocinadorId, authentication.principal)")
    public ResponseEntity<List<ConvidadoResponse>> findByPatrocinador(@PathVariable Long patrocinadorId) {
        return ResponseEntity.ok(convidadoService.findByPatrocinadorId(patrocinadorId));
    }

    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmarPresenca(@RequestParam String token) {
        convidadoService.confirmarPresenca(token);

        return ResponseEntity.status(HttpStatus.OK).body("E-mail confirmado com sucesso. Seu ingresso foi gerado e enviado para o seu e-mail.");
    }
}
