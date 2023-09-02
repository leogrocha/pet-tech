package br.com.fiap.pettech.dominio.endereco.dto;

import br.com.fiap.pettech.dominio.endereco.entity.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(

        Long id,
        @NotBlank(message = "A rua não poder estar em branco")
        String rua,
        @NotBlank(message = "A cidade não poder estar em branco")
        String cidade,
        @NotBlank(message = "O estado não poder estar em branco")
        @Size(min = 2, max = 2, message = "Um estado deve ter exatamente 2 caracteres.")
        String estado,
        @NotBlank(message = "O cep não poder estar em branco")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "O cep deve ser estar no formato 00000-000")
        String cep
) {

    public static Endereco toEntity(EnderecoDTO dto) {
        return new Endereco(dto);
    }

    public static EnderecoDTO fromEntity(Endereco endereco) {
        return new EnderecoDTO(endereco.getId(), endereco.getRua(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
    }

    public static Endereco mapperDtoToEntity(EnderecoDTO enderecoDTO, Endereco endereco) {
        endereco.setRua(enderecoDTO.rua());
        endereco.setCidade(enderecoDTO.cidade());
        endereco.setEstado(enderecoDTO.estado());
        endereco.setCep(enderecoDTO.cep());
        return endereco;
    }


}
