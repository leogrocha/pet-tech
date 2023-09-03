package br.com.fiap.pettech.dominio.pessoa.dto;

import br.com.fiap.pettech.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.pettech.dominio.pessoa.entity.Pessoa;
import br.com.fiap.pettech.dominio.usuario.dto.UsuarioDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record PessoaEnderecoUsuarioDTO(

        Long id,

        @NotBlank(message = "Nome não pode ser vazio")
        String nome,

        @NotNull(message = "Data de nascimento não pode ser vazia")
        @PastOrPresent(message = "Nascimento não pode ser uma data maior do que a atual")
        LocalDate nascimento,

        @NotBlank(message = "CPF não pode ser vazio")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "E-mail não pode ser vazio")
        @Email(message = "E-mail tem que ser valido")
        String email,

        Set<EnderecoDTO> enderecos,
        UsuarioDTO usuario
) {

    public static PessoaEnderecoUsuarioDTO fromEntity(Pessoa pessoa) {
        Set<EnderecoDTO> enderecos = new HashSet<>();

        if(!pessoa.getEnderecos().isEmpty()) {
            pessoa.getEnderecos().forEach(endereco -> { enderecos.add(EnderecoDTO.fromEntity(endereco));});
        }

        return new PessoaEnderecoUsuarioDTO(pessoa.getId(), pessoa.getNome(), pessoa.getNascimento(), pessoa.getCpf(), pessoa.getEmail(), enderecos
        , pessoa.getUsuario() != null ? new UsuarioDTO(pessoa.getUsuario()) : null);
    }
}
