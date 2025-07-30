package com.devioz.controller;

import com.devioz.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtils jwtUtils;

    @GetMapping
    public UsuarioInfo getUserInfo() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new UsuarioInfo(principal.getUsername(), principal.getAuthorities());
    }

    public static class UsuarioInfo {
        private final String username;
        private final List<String> roles;

        public UsuarioInfo(String username, Collection<? extends GrantedAuthority> authorities) {
            this.username = username;
            this.roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }

        public String getUsername() {
            return username;
        }

        public List<String> getRoles() {
            return roles;
        }
    }
}
