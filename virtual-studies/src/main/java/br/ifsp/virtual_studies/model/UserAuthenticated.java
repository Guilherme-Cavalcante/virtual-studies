package br.ifsp.virtual_studies.model;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserAuthenticated implements UserDetails {

    private final User user;

    public UserAuthenticated(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> aroles = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        roles.add(user.getRole());
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
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
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