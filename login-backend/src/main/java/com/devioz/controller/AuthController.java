package com.devioz.controller;

import com.devioz.model.Usuario;
import com.devioz.repository.UsuarioRepository;
import com.devioz.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = (User) auth.getPrincipal();
        String token = jwtUtils.generarToken(user.getUsername(), user.getAuthorities().iterator().next().getAuthority());

        return new JwtResponse(token);
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody LoginRequest request) {
//        // 1. Verifica si el usuario ya existe
//        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
//            return ResponseEntity
//                    .status(HttpStatus.CONFLICT)
//                    .body("El nombre de usuario ya está en uso.");
//        }
//
//        // 2. Crea el nuevo usuario con contraseña encriptada
//        Usuario nuevoUsuario = new Usuario();
//        nuevoUsuario.setUsername(request.username());
//        nuevoUsuario.setPassword(passwordEncoder.encode(request.password()));
//        nuevoUsuario.setRol("USER"); // Rol por defecto
//
//        usuarioRepository.save(nuevoUsuario);
//
//        return ResponseEntity.ok("Usuario registrado exitosamente.");
//    }
//

    public record LoginRequest(String username, String password) {}
    public record JwtResponse(String token) {}
}
