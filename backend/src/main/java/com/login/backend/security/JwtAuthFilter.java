package com.login.backend.security;

import com.login.backend.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService; // Usaremos tu UserDetailsServiceImpl

    // Constructor inyectando JwtUtil y UserDetailsServiceImpl
    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Verifica si el encabezado de autorización existe y empieza con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extrae el token
            try {
                username = jwtUtil.extractUsername(token); // Extrae el nombre de usuario del token
            } catch (Exception e) {
                // Si hay un error al extraer el usuario (token inválido/expirado), imprimirlo
                System.out.println("Error al procesar el token JWT: " + e.getMessage());
            }
        }

        // Si se extrajo un nombre de usuario y no hay autenticación actual en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario usando nuestro servicio
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Valida el token contra los detalles del usuario cargados
            if (jwtUtil.validateToken(token, userDetails)) {
                // Si el token es válido, crea un objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // Establece detalles adicionales de la solicitud
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Establece la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continúa con la cadena de filtros de Spring Security
        filterChain.doFilter(request, response);
    }
}
