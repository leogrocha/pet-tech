package br.com.fiap.pettech.dominio.pessoa.service;

import br.com.fiap.pettech.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.pettech.dominio.endereco.entity.Endereco;
import br.com.fiap.pettech.dominio.pessoa.dto.PessoaDTO;
import br.com.fiap.pettech.dominio.pessoa.entity.Pessoa;
import br.com.fiap.pettech.dominio.pessoa.repository.IPessoaRepository;
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

    @Autowired
    public PessoaService(IPessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional(readOnly = true)
    public Page<PessoaDTO> findAll(PageRequest pageRequest) {
        Page<Pessoa> list = pessoaRepository.findAll(pageRequest);
        return list.map(PessoaDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public PessoaDTO findById(Long id) {
        return pessoaRepository.findById(id).map(PessoaDTO::fromEntity)
                .orElseThrow(() -> new ControllerNotFoundException("Pessoa não encontrada"));
    }

    @Transactional
    public PessoaDTO save(PessoaDTO pessoaDTO) {
        Pessoa entity = PessoaDTO.toEntity(pessoaDTO);
        var pessoaSaved = pessoaRepository.save(entity);
        return PessoaDTO.fromEntity(pessoaSaved);
    }

    @Transactional
    public PessoaDTO update(Long id, PessoaDTO pessoaDTO) {

        try {
            Pessoa pessoa = pessoaRepository.getReferenceById(id);
            PessoaDTO.mapperDtoToEntity(pessoaDTO, pessoa);
            pessoa = pessoaRepository.save(pessoa);
            return PessoaDTO.fromEntity(pessoa);
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
