package br.ifsp.virtual_studies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Virtual Studies API", version = "1.0", description = "Documentação da API de Virtual Studies"))
@SpringBootApplication
public class VirtualStudiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualStudiesApplication.class, args);
	}

}
