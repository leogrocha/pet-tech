package br.com.fiap.pettech.dominio.pessoa.repository;

import br.com.fiap.pettech.dominio.pessoa.entity.Pessoa;

import br.com.fiap.pettech.exception.service.ControllerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

@Repository
public class PessoaRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PessoaRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<Pessoa> findAll(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM pessoas limit ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{pageSize, offset}, new PessoaRowMapper());
    }


    public Pessoa findById(Long id) {
        String sql = "SELECT * FROM pessoas WHERE id = ?";
        List<Pessoa> pessoas = 
        jdbcTemplate.query(sql, new Object[]{id}, new PessoaRowMapper());
        return pessoas.isEmpty() ? null : pessoas.get(0);
    }


    public Pessoa save(Pessoa pessoa) {
        
        try {
            String sql = "INSERT INTO pessoas (cpf, nome, nascimento, email) values (?,?,?,?)";
            this.jdbcTemplate.update(sql, pessoa.getCpf(), pessoa.getNome(), Date.valueOf(pessoa.getNascimento()), pessoa.getEmail());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return pessoa;
    }


    public Pessoa update(Long id, Pessoa pessoa) {
        
        String sql = "UPDATE pessoas SET cpf = ?, nome = ?, nascimento = ?, email = ? WHERE id = ?";
        int rowAffected = jdbcTemplate.update(sql, pessoa.getCpf(), pessoa.getNome(), pessoa.getNascimento(),
                pessoa.getEmail(), id);
        if (rowAffected == 0) {
            throw new ControllerNotFoundException("Pessoa com ID: " + id + " não encontrada");
        }

        return pessoa;
    }


    public void deleteById(Long id) {
        String sql = "DELETE FROM pessoas where id = ?";
        int rowAffected = jdbcTemplate.update(sql, id);
        if (rowAffected == 0) {
            throw new ControllerNotFoundException("Pessoa com ID: " + id + " não encontrada");
        }
    }

    private static class PessoaRowMapper implements RowMapper<Pessoa> {
        @Override
        public Pessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(rs.getLong("id"));
            pessoa.setCpf(rs.getString("cpf"));
            pessoa.setNome(rs.getString("nome"));
            pessoa.setEmail(rs.getString("email"));
            pessoa.setNascimento(rs.getDate("nascimento").toLocalDate());
            return pessoa;
        }
    }

}
