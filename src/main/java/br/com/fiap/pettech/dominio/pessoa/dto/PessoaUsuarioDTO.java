package br.com.fiap.pettech.dominio.pessoa.dto;

import br.com.fiap.pettech.dominio.pessoa.entity.Pessoa;
import br.com.fiap.pettech.dominio.usuario.dto.UsuarioDTO;
import br.com.fiap.pettech.dominio.usuario.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PessoaUsuarioDTO(

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

        UsuarioDTO usuario
) {

    public static Pessoa toEntity(PessoaUsuarioDTO pessoaDTO, Usuario usuario) {
        return new Pessoa(pessoaDTO, usuario);
    }

    public static PessoaUsuarioDTO fromEntity(Pessoa pessoa) {


        return new PessoaUsuarioDTO(pessoa.getId(), pessoa.getNome(), pessoa.getNascimento(), pessoa.getCpf(), pessoa.getEmail(),
        pessoa.getUsuario() != null ? new UsuarioDTO(pessoa.getUsuario()) : null);
    }

    public static Pessoa mapperDtoToEntity(PessoaUsuarioDTO pessoaDTO, Pessoa pessoa, Usuario usuario) {
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setNascimento(pessoaDTO.nascimento());
        pessoa.setCpf(pessoaDTO.cpf());
        pessoa.setEmail(pessoaDTO.email());
        pessoa.setUsuario(usuario);
        return pessoa;
    }

}
