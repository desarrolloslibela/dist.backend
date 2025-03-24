package com.proj.backend.service;

import com.proj.backend.dto.AuthResponseDTO;
import com.proj.backend.model.Role;
import com.proj.backend.model.RoleType;
import com.proj.backend.model.User;
import com.proj.backend.repository.RoleRepository;
import com.proj.backend.repository.UserRepository;
import com.proj.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final Set<String> invalidatedTokens = new HashSet<>();


    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(String nombre, String apellido, String email, String password, RoleType roleType) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El usuario ya existe.");
        }

        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = new User(nombre, apellido, email, passwordEncoder.encode(password), role);
        userRepository.save(user);

    }

    public AuthResponseDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas.");
        }

        String token = jwtUtil.generateToken(email, user.getRole().getName().name());
        return new AuthResponseDTO(
                token,
                user.getRole().getName().name(),
                user.getEmail(),
                user.getNombre(),
                user.getApellido()
        );
    }

    public void logout(String token) {
        jwtUtil.invalidateToken(token);
    }

    public boolean isAuthenticated(String token) {
        return jwtUtil.isTokenValid(token);
    }

}
