package br.ifsp.virtual_studies.dto.chat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequestDTO {
    
    @NotBlank(message = "Campo 'subject' não pode estar vazio")
    private String subject;
}
