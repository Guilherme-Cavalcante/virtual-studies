package br.ifsp.virtual_studies.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.virtual_studies.dto.usuario.UsuarioRegistrationDTO;
import br.ifsp.virtual_studies.dto.usuario.UsuarioResponseDTO;
import br.ifsp.virtual_studies.service.UsuarioService;
import jakarta.validation.Valid;

@Deprecated
@RestController
@RequestMapping("/api/users/register")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registerUser(@Valid @RequestBody UsuarioRegistrationDTO usuario) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
    }
}
