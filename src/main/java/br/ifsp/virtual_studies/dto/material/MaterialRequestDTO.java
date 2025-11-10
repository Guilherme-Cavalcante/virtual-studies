package br.ifsp.virtual_studies.dto.material;
                
import br.ifsp.virtual_studies.model.Chat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialRequestDTO {
    
    @NotNull(message = "Campo 'title' não pode ser nulo")
    private String title;
    
    @NotNull(message = "Campo 'description' não pode ser nulo")
    private String description;

    @NotNull(message = "Campo 'text' não pode ser nulo")
    private Chat chat;
    
    @NotNull(message = "Campo 'local' não pode ser nulo")
    private String local;
}
