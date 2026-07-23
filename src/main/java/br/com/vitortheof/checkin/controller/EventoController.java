package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.EventoRequest;
import br.com.vitortheof.checkin.dto.response.EventoResponse;
import br.com.vitortheof.checkin.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponse> createEvento(@Valid  @RequestBody EventoRequest request) {
        EventoResponse response = eventoService.createEvento(request);
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
}
