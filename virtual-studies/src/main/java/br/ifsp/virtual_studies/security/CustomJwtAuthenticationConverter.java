package br.ifsp.virtual_studies.security;

import br.ifsp.virtual_studies.model.Usuario;
import br.ifsp.virtual_studies.model.UsuarioAuthenticated;
import br.ifsp.virtual_studies.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UsuarioAuthenticated userAuthenticated = extractUser(jwt);
        List<GrantedAuthority> authorities = List.copyOf(userAuthenticated.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userAuthenticated, null, authorities);
    }
    
    private UsuarioAuthenticated extractUser(Jwt jwt) {
        Long userId = jwt.getClaim("userId");
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        return new UsuarioAuthenticated(user);
    }
}