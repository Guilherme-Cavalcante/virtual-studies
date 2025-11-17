package br.ifsp.virtual_studies.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.model.Usuario;
import br.ifsp.virtual_studies.model.UsuarioAuthenticated;
import br.ifsp.virtual_studies.repository.UsuarioRepository;

@Service
public class UserDetailService implements UserDetailsService {
    private final UsuarioRepository userRepository;

    private UserDetailService(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UsuarioAuthenticated(user);
    }
}