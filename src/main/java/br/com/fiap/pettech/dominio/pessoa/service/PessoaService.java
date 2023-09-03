package br.com.fiap.pettech.dominio.pessoa.service;

import br.com.fiap.pettech.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.pettech.dominio.endereco.entity.Endereco;
import br.com.fiap.pettech.dominio.pessoa.dto.PessoaDTO;
import br.com.fiap.pettech.dominio.pessoa.dto.PessoaEnderecoDTO;
import br.com.fiap.pettech.dominio.pessoa.dto.PessoaUsuarioDTO;
import br.com.fiap.pettech.dominio.pessoa.entity.Pessoa;
import br.com.fiap.pettech.dominio.pessoa.repository.IPessoaRepository;
import br.com.fiap.pettech.dominio.usuario.repository.UsuarioRepository;
import br.com.fiap.pettech.exception.service.ControllerNotFoundException;
import br.com.fiap.pettech.exception.service.DatabaseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaService {

    private final IPessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PessoaService(IPessoaRepository pessoaRepository, UsuarioRepository usuarioRepository) {
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Page<PessoaEnderecoDTO> findAll(PageRequest pageRequest) {
        Page<Pessoa> list = pessoaRepository.findAll(pageRequest);
        return list.map(PessoaEnderecoDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public PessoaEnderecoDTO findById(Long id) {
        return pessoaRepository.findById(id).map(PessoaEnderecoDTO::fromEntity)
                .orElseThrow(() -> new ControllerNotFoundException("Pessoa não encontrada"));
    }

    @Transactional
    public PessoaUsuarioDTO save(PessoaUsuarioDTO pessoaUsuarioDTO) {

        try {
            var usuario = usuarioRepository.getReferenceById(pessoaUsuarioDTO.usuario().id());
            Pessoa entity = PessoaUsuarioDTO.toEntity(pessoaUsuarioDTO, usuario);
            var pessoaSaved = pessoaRepository.save(entity);
            return PessoaUsuarioDTO.fromEntity(pessoaSaved);

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Usuário não encontrado");
        }

    }

    @Transactional
    public PessoaUsuarioDTO update(Long id, PessoaUsuarioDTO pessoaUsuarioDTO) {

        try {
            var usuario = usuarioRepository.getReferenceById(pessoaUsuarioDTO.usuario().id());
            Pessoa pessoa = pessoaRepository.getReferenceById(id);
            PessoaUsuarioDTO.mapperDtoToEntity(pessoaUsuarioDTO, pessoa, usuario);
            pessoa = pessoaRepository.save(pessoa);
            return PessoaUsuarioDTO.fromEntity(pessoa);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Pessoa não encontrada, id: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            pessoaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade de dados");
        }
    }




}
