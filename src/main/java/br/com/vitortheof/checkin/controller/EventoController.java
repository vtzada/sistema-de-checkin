package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.EventoRequest;
import br.com.vitortheof.checkin.dto.response.EventoResponse;
import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.repository.EventoRepository;
import br.com.vitortheof.checkin.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final EventoRepository eventoRepository;

    @PostMapping
    @PreAuthorize("hasRole('PRODUTOR') or hasRole('ADMIN')")
    public ResponseEntity<EventoResponse> createEvento(@Valid  @RequestBody EventoRequest request, @AuthenticationPrincipal Usuario usuarioLogado) {
        EventoResponse response = eventoService.createEvento(request, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> findById(@PathVariable Long id){
        EventoResponse response = eventoService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventoResponse>> findAll(){
        List<EventoResponse> response = eventoService.findAll();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfEvento(#id, authentication.principal)")
    public ResponseEntity<EventoResponse> updateEvento(@PathVariable Long id, @Valid @RequestBody EventoRequest request, @AuthenticationPrincipal Usuario usuarioLogado){

        EventoResponse response = eventoService.updateEvento(id, request);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("@eventSecurity.isOwnerOrAdminOfEvento(#id, authentication.principal)")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        eventoService.deleteEvento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meus-eventos")
    public ResponseEntity<List<Evento>> listarMeusEventos(@AuthenticationPrincipal Usuario usuario) {
        List<Evento> eventos = eventoRepository.findByProdutorId(usuario.getId());
        return ResponseEntity.ok(eventos);
    }
}
