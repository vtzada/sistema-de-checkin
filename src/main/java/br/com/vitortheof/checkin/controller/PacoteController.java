package br.com.vitortheof.checkin.controller;


import br.com.vitortheof.checkin.dto.request.PacoteRequest;
import br.com.vitortheof.checkin.dto.response.PacoteResponse;
import br.com.vitortheof.checkin.service.PacoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento/pacote")
@RequiredArgsConstructor
public class PacoteController {

    private final PacoteService pacoteService;

    @PostMapping
    public ResponseEntity<PacoteResponse> createPacote(@Valid  @RequestBody PacoteRequest pacoteRequest) {
        PacoteResponse response = pacoteService.createPacote(pacoteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacoteResponse> findById(@PathVariable Long id) {
        PacoteResponse response = pacoteService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PacoteResponse>> findAllPacotes() {
        List<PacoteResponse> response = pacoteService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
