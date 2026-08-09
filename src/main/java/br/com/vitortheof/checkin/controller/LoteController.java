package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.LoteRequest;
import br.com.vitortheof.checkin.dto.response.LoteResponse;
import br.com.vitortheof.checkin.service.LoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoteController {

    private final LoteService loteService;

    @PostMapping("/tipo-ingresso/{tipoIngressoId}/lote")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOwnerOrAdminOfTipoIngresso(#tipoIngressoId, authentication.principal)")
    public ResponseEntity<LoteResponse> create(@PathVariable Long tipoIngressoId,
                                               @Valid @RequestBody LoteRequest loteRequest) {
        LoteResponse response = loteService.createLote(tipoIngressoId, loteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/tipo-ingresso/{tipoIngressoId}/lotes")
    public ResponseEntity<List<LoteResponse>> findByTipoIngresso(@PathVariable Long tipoIngressoId) {
        return ResponseEntity.ok(loteService.findByTipoIngressoId(tipoIngressoId));
    }

    @GetMapping("/lote/{id}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOwnerOrAdminOfLote(#id, authentication.principal)")
    public ResponseEntity<LoteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(loteService.findById(id));
    }

    @GetMapping("/evento/{eventoId}/lotes-disponiveis")
    public ResponseEntity<List<LoteResponse>> findDisponiveisByEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(loteService.findDisponiveisByEventoId(eventoId));
    }
}
