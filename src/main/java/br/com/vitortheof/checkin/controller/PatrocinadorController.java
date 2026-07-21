package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.PatrocinadorRequest;
import br.com.vitortheof.checkin.dto.response.PatrocinadorResponse;
import br.com.vitortheof.checkin.service.PatrocinadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evento/patrocinador")
@RequiredArgsConstructor
public class PatrocinadorController {

    private final PatrocinadorService patrocinadorService;

    @PostMapping
    public ResponseEntity<PatrocinadorResponse> createPatrocinador(@RequestBody PatrocinadorRequest request) {
        PatrocinadorResponse response = patrocinadorService.createPatrocinador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatrocinadorResponse> findById(@PathVariable Long id) {
        PatrocinadorResponse response = patrocinadorService.findById(id);
        return ResponseEntity.ok(response);
    }

}
