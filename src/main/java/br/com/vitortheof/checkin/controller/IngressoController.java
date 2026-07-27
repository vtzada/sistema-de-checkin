package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.response.IngressoResponse;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.service.IngressoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/evento/ingressos")
@RequiredArgsConstructor
public class IngressoController {

    private final IngressoService ingressoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IngressoResponse>> findAll() {
        List<IngressoResponse> response = ingressoService.findAll();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/checkin")
    @PreAuthorize("@eventSecurity.canCheckInByQr(#codigoQR, authentication.principal)")
    public ResponseEntity<IngressoResponse> realizarCheckin(@RequestParam UUID codigoQR, @AuthenticationPrincipal Usuario usuarioLogado) {
        IngressoResponse response = ingressoService.realizarCheckin(codigoQR);
        return ResponseEntity.ok(response);
    }
}
