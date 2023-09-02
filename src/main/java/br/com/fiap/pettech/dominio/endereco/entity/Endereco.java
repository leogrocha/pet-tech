package br.com.fiap.pettech.dominio.endereco.entity;

import br.com.fiap.pettech.dominio.endereco.dto.EnderecoDTO;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rua;

    private String cidade;

    private String estado;

    private String cep;

    public Endereco(){}

    public Endereco(Long id, String rua, String cidade, String estado, String cep) {
        this.id = id;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Endereco(EnderecoDTO dto) {
        id = dto.id();
        rua = dto.rua();
        cidade = dto.cidade();
        estado = dto.estado();
        cep = dto.cep();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endereco endereco = (Endereco) o;

        if (!Objects.equals(id, endereco.id)) return false;
        if (!Objects.equals(rua, endereco.rua)) return false;
        if (!Objects.equals(cidade, endereco.cidade)) return false;
        if (!Objects.equals(estado, endereco.estado)) return false;
        return Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rua != null ? rua.hashCode() : 0);
        result = 31 * result + (cidade != null ? cidade.hashCode() : 0);
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        result = 31 * result + (cep != null ? cep.hashCode() : 0);
        return result;
    }
}
