package com.proj.backend.controller;

import com.proj.backend.dto.AuthResponseDTO;
import com.proj.backend.dto.LoginRequestDTO;
import com.proj.backend.dto.RegisterRequestDTO;
import com.proj.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {
       authService.register(
                request.getNombre(),
                request.getApellido(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );

        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.badRequest().body("No se encontró el token en la petición");
        }
        authService.logout(token);
        return ResponseEntity.ok("Sesión cerrada exitosamente.");
    }


    @GetMapping("/status")
    public ResponseEntity<String> getStatus(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !authService.isAuthenticated(token)) {
            return ResponseEntity.status(401).body("Usuario no autenticado.");
        }
        return ResponseEntity.ok("Usuario autenticado.");
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
