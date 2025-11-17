package br.ifsp.virtual_studies.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.model.Usuario;
import br.ifsp.virtual_studies.repository.UsuarioRepository;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final UsuarioRepository userRepository;
    
    public AuthenticationService(JwtService jwtService, UsuarioRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    
    public String authenticate(Authentication authentication) {
        String username = authentication.getName();     
        Usuario user = userRepository.findByName(username)
            .orElseThrow(() -> new RuntimeException("Usuario not found"));
        return jwtService.generateToken(user);
    }
}
