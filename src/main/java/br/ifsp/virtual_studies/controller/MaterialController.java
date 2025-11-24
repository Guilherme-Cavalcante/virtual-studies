// package br.ifsp.virtual_studies.controller;

// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import br.ifsp.virtual_studies.dto.material.MaterialResponseDTO;
// import br.ifsp.virtual_studies.dto.material.MaterialRequestDTO;
// import br.ifsp.virtual_studies.dto.page.PagedResponse;
// import br.ifsp.virtual_studies.service.MaterialService;
// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/materials")
// public class MaterialController {
//     private final MaterialService materialService;
    
//     public MaterialController(MaterialService materialService) {
//         this.materialService = materialService;
//     }
    
//     @PostMapping
//     public ResponseEntity<MaterialResponseDTO> createMaterial(@Valid @RequestBody MaterialRequestDTO material) {
//         MaterialResponseDTO materialResponseDTO = materialService.createMaterial(material);
//         return ResponseEntity.status(HttpStatus.CREATED).body(materialResponseDTO);
//     }
    
//     @GetMapping
//     public ResponseEntity<PagedResponse<MaterialResponseDTO>> getAllMaterials(Pageable pageable) {
//         return ResponseEntity.ok(materialService.getAllMaterials(pageable));
//     }
    
//     @GetMapping("/{id}")
//     public ResponseEntity<MaterialResponseDTO> getMaterialById(@PathVariable Long id) {
//         return ResponseEntity.ok(materialService.getMaterialById(id));
//     }
    
//     @PutMapping("/{id}")
//     public ResponseEntity<MaterialResponseDTO> updateMaterial(@PathVariable Long id,
//             @Valid @RequestBody MaterialRequestDTO materialDto) {
//         MaterialResponseDTO updatedMaterial = materialService.updateMaterial(id, materialDto);
//         return ResponseEntity.ok(updatedMaterial);
//     }
    
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
//         materialService.deleteMaterial(id);
//         return ResponseEntity.noContent().build();
//     }
// }
