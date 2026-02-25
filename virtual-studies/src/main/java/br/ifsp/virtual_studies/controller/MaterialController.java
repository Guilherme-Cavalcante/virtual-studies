package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.virtual_studies.dto.material.MaterialResponseDTO;
import br.ifsp.virtual_studies.dto.material.MaterialPatchDTO;
import br.ifsp.virtual_studies.dto.material.MaterialRequestDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.model.UserAuthenticated;
import br.ifsp.virtual_studies.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/materials")
@Tag(name = "Materials")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @Operation(summary = "Fetch material")
    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> getMaterialById(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        return ResponseEntity.ok(materialService.getMaterialById(id, authentication.getUser()));
    }

    @Operation(summary = "Update material")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResponseDTO> updateMaterial(@PathVariable Long id,
            @Valid @RequestBody MaterialPatchDTO materialDto,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        MaterialResponseDTO updatedMaterial = materialService.updateMaterial(id, materialDto, authentication.getUser());
        return ResponseEntity.ok(updatedMaterial);
    }

    @Operation(summary = "Delete material")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        materialService.deleteMaterial(id, authentication.getUser());
        return ResponseEntity.noContent().build();
    }
}
