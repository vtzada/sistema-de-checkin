package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.IngressoResponse;
import br.com.vitortheof.checkin.dto.response.IngressoRequest;
import br.com.vitortheof.checkin.service.IngressoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento/ingressos")
@RequiredArgsConstructor
public class IngressoController {

    private final IngressoService ingressoService;

    @PostMapping
    public ResponseEntity<IngressoResponse> createIngresso(@RequestBody IngressoRequest request) {
        IngressoResponse response = ingressoService.createIngresso(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IngressoResponse>> findAll() {
        List<IngressoResponse> response = ingressoService.findAll();
        return ResponseEntity.ok().body(response);
    }
}
