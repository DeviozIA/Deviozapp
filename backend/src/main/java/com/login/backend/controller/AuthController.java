package com.login.backend.controller;

import com.login.backend.security.JwtUtil;
import com.login.backend.service.UserDetailsServiceImpl;
import com.login.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil; // Inyecta JwtUtil
    private final UserDetailsServiceImpl userDetailsService; // Inyecta UserDetailsServiceImpl

    // Constructor actualizado
    public AuthController(UserService userService, AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // --- DTOs (Data Transfer Objects) para solicitudes ---
    public static class RegisterRequest {
        @NotBlank(message = "El nombre es requerido")
        private String firstName;
        @NotBlank(message = "El apellido es requerido")
        private String lastName;
        @Email(message = "Formato de email inválido")
        @NotBlank(message = "El email es requerido")
        private String email;
        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        private String password;
        @NotBlank(message = "La confirmación de email es requerida")
        private String confirmEmail;

        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getConfirmEmail() {
            return confirmEmail;
        }
        public void setConfirmEmail(String confirmEmail) {
            this.confirmEmail = confirmEmail;
        }
    }

    public static class LoginRequest {
        @Email(message = "Formato de email inválido")
        @NotBlank(message = "El email es requerido")
        private String email;
        @NotBlank(message = "La contraseña es requerida")
        private String password;

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO para la respuesta del login (incluye el JWT)
    public static class JwtResponse {
        private String token;
        private String message;

        public JwtResponse(String token, String message) {
            this.token = token;
            this.message = message;
        }

        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }

    // --- Endpoints REST ---

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (!registerRequest.getEmail().equalsIgnoreCase(registerRequest.getConfirmEmail())) {
            return ResponseEntity.badRequest().body("Los emails no coinciden.");
        }
        try {
            userService.registerNewUser(
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
            );
            return ResponseEntity.ok("Usuario registrado exitosamente!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Carga los UserDetails para generar el token
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String jwt = jwtUtil.generateToken(userDetails); // Genera el JWT

            // Devuelve el token en la respuesta
            return ResponseEntity.ok(new JwtResponse(jwt, "Inicio de sesión exitoso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contraseña inválidos.");
        }
    }

    // Nuevo endpoint de prueba para ver el funcionamiento de JWT
    @PostMapping("/test-protected")
    public ResponseEntity<String> testProtectedEndpoint() {
        // Este endpoint solo será accesible si se envía un JWT válido en el header Authorization
        return ResponseEntity.ok("¡Acceso a recurso protegido exitoso!");
    }
}