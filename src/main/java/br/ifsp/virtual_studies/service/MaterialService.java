package br.ifsp.virtual_studies.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.material.MaterialRequestDTO;
import br.ifsp.virtual_studies.dto.material.MaterialResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Material;
import br.ifsp.virtual_studies.repository.MaterialRepository;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public MaterialService(MaterialRepository materialRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.materialRepository = materialRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public MaterialResponseDTO createMaterial(MaterialRequestDTO materialDto) {
        Material material = modelMapper.map(materialDto, Material.class);
        Material createdMaterial = materialRepository.save(material);
        return modelMapper.map(createdMaterial, MaterialResponseDTO.class);
    }
    
    public PagedResponse<MaterialResponseDTO> getAllMaterials(Pageable pageable) {
        Page<Material> materialsPage = materialRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(materialsPage, MaterialResponseDTO.class);
    }
    
    public MaterialResponseDTO getMaterialById(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material not found"));
        return modelMapper.map(material, MaterialResponseDTO.class);
    }
    
    public MaterialResponseDTO updateMaterial(Long id, MaterialRequestDTO materialDto) {
        
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + id));
        
        modelMapper.map(materialDto, existingMaterial);
        existingMaterial.setId(id);
        Material updatedMaterial = materialRepository.save(existingMaterial);
        return modelMapper.map(updatedMaterial, MaterialResponseDTO.class);
    }
    
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + id));
        
        materialRepository.delete(material);
    }
}