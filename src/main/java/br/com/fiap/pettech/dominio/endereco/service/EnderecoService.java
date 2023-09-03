package br.com.fiap.pettech.dominio.endereco.service;

import br.com.fiap.pettech.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.pettech.dominio.endereco.dto.EnderecoPessoaDTO;
import br.com.fiap.pettech.dominio.endereco.entity.Endereco;
import br.com.fiap.pettech.dominio.endereco.repository.EnderecoRepository;
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
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final IPessoaRepository pessoaRepository;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository, IPessoaRepository pessoaRepository) {
        this.enderecoRepository = enderecoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional(readOnly = true)
    public Page<EnderecoPessoaDTO> findAll(PageRequest pageRequest) {
        Page<Endereco> list = enderecoRepository.findAll(pageRequest);
        return list.map(EnderecoPessoaDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public EnderecoPessoaDTO findById(Long id) {
//        return  enderecoRepository.findById(id).map(EnderecoDTO::fromEntity)
//                .orElseThrow(() -> new ConcurrentModificationException("Endereco não encontrado"));

        var endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException("Endereço não encontrado"));
        return EnderecoPessoaDTO.fromEntity(endereco);
    }

    @Transactional
    public EnderecoPessoaDTO save(EnderecoPessoaDTO dto) {

        try {
        var pessoa = pessoaRepository.getReferenceById(dto.pessoa().id());
        var entity = EnderecoPessoaDTO.toEntity(dto, pessoa);
        var enderecoSaved = enderecoRepository.save(entity);
        return EnderecoPessoaDTO.fromEntity(enderecoSaved);

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Pessoa não encontrada");
        }
    }

    @Transactional
    public EnderecoPessoaDTO update(Long id, EnderecoPessoaDTO dto) {

        try {
            var endereco = enderecoRepository.getReferenceById(id);
            var pessoa = pessoaRepository.getReferenceById(dto.pessoa().id());
            EnderecoPessoaDTO.mapperDtoToEntity(dto, endereco, pessoa);
            endereco = enderecoRepository.save(endereco);
            return EnderecoPessoaDTO.fromEntity(endereco);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Pessoa/Endereço não encontrado");
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            enderecoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade de dados");
        }
    }

}
