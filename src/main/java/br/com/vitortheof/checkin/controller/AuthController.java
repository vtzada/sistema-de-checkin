package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.AuthRequest;
import br.com.vitortheof.checkin.dto.request.RegisterRequest;
import br.com.vitortheof.checkin.dto.response.AuthResponse;
import br.com.vitortheof.checkin.infra.security.TokenService;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthRequest request) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest request) {
        if (this.repository.findByEmail(request.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.senha());
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.nome());
        novoUsuario.setEmail(request.email());
        novoUsuario.setSenha(encryptedPassword);
        novoUsuario.setRole(request.role());

        this.repository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }

}
