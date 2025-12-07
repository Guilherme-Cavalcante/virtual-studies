package br.ifsp.virtual_studies.model;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioAuthenticated implements UserDetails {

    private final Usuario usuario;

    public UsuarioAuthenticated(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> aroles = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        roles.add(usuario.getRole());
        roles.add(Role.STUDENT);
        aroles = roles.stream()
                .map(role -> (GrantedAuthority) () -> role.toString())
                .toList();
        Set<GrantedAuthority> setroles = new HashSet<>();
        for (GrantedAuthority a : aroles) {
            setroles.add(a);
        }
        return setroles;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}