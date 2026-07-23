package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.response.IngressoResponse;
import br.com.vitortheof.checkin.service.IngressoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/evento/ingressos")
@RequiredArgsConstructor
public class IngressoController {

    private final IngressoService ingressoService;
    @GetMapping
    public ResponseEntity<List<IngressoResponse>> findAll() {
        List<IngressoResponse> response = ingressoService.findAll();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/checkin")
    public ResponseEntity<IngressoResponse> realizarCheckin(@RequestParam UUID codigoQR) {
        IngressoResponse response = ingressoService.realizarCheckin(codigoQR);
        return ResponseEntity.ok(response);
    }
}
