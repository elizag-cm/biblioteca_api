package com.projeto.biblioteca.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.biblioteca.entity.Usuario;
import com.projeto.biblioteca.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (!usuarioOpt.isPresent()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        return new UserDetailsImpl(usuario);
    }
}