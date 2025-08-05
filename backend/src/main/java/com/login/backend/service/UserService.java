package com.login.backend.service;

import com.login.backend.model.Role;
import com.login.backend.model.User;
import com.login.backend.repository.RoleRepository;
import com.login.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(String firstName, String lastName, String email, String password) throws Exception {
        if (userRepository.existsByEmail(email)) {
            throw new Exception("El email ya está registrado."); // El email ya existe.
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Codifica la contraseña antes de guardar.

        // Asigna el rol por defecto (ROLE_USER) al nuevo usuario.
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado.")); // Asegura que el rol 'ROLE_USER' exista.
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user); // Guarda el usuario en la base de datos.
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email); // Busca un usuario por email.
    }
}
