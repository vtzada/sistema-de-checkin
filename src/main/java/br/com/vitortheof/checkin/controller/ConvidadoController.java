package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.ConvidadoRequest;
import br.com.vitortheof.checkin.dto.response.ConvidadoResponse;
import br.com.vitortheof.checkin.service.ConvidadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento/convidado")
@RequiredArgsConstructor
public class ConvidadoController {

    private final ConvidadoService convidadoService;

    @PostMapping
    public ResponseEntity<ConvidadoResponse> createConvidado(@RequestBody ConvidadoRequest request){
        ConvidadoResponse response = convidadoService.createConvidado(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConvidadoResponse> findById(@PathVariable Long id){
        ConvidadoResponse response = convidadoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConvidadoResponse>> findAll(){
        List<ConvidadoResponse> response = convidadoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
