package br.com.fiap.pettech.dominio.pessoa.dto;

import br.com.fiap.pettech.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.pettech.dominio.endereco.entity.Endereco;
import br.com.fiap.pettech.dominio.pessoa.entity.Pessoa;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PessoaDTO(

        Long id,

        @NotBlank(message = "Nome não pode ser vazio")
        String nome,

        @NotBlank(message = "Data de nascimento não pode ser vazia")
        @PastOrPresent(message = "Nascimento não pode ser uma data maior do que a atual")
        LocalDate nascimento,

        @NotBlank(message = "CPF não pode ser vazio")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "E-mail não pode ser vazio")
        @Email(message = "E-mail tem que ser valido")
        String email
) {

    public static Pessoa toEntity(PessoaDTO pessoaDTO) {
        return new Pessoa(pessoaDTO);
    }

    public static PessoaDTO fromEntity(Pessoa pessoa) {
        return new PessoaDTO(pessoa.getId(), pessoa.getNome(), pessoa.getNascimento(), pessoa.getCpf(), pessoa.getEmail());
    }

    public static Pessoa mapperDtoToEntity(PessoaDTO pessoaDTO, Pessoa pessoa) {
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setNascimento(pessoaDTO.nascimento());
        pessoa.setCpf(pessoaDTO.cpf());
        pessoa.setEmail(pessoaDTO.email());
        return pessoa;
    }

}
