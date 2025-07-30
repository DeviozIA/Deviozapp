package com.devioz.config;

import com.devioz.model.Usuario;
import com.devioz.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initUsuarios(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                Usuario usuario = Usuario.builder()
                        .username("admin")
                        .password(encoder.encode("123456"))
                        .rol("ADMIN")
                        .habilitado(true)
                        .build();
                repo.save(usuario);
            }
            // Crea usuario USER si no existe
            if (repo.findByUsername("usuario").isEmpty()) {
                Usuario user = Usuario.builder()
                        .username("usuario")
                        .password(encoder.encode("123456"))
                        .rol("USER")
                        .habilitado(true)
                        .build();
                repo.save(user);
            }
        };
    }
}
