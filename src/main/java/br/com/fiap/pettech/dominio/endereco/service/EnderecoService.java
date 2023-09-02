package br.com.fiap.pettech.dominio.endereco.service;

import br.com.fiap.pettech.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.pettech.dominio.endereco.entity.Endereco;
import br.com.fiap.pettech.dominio.endereco.repository.EnderecoRepository;
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
    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional(readOnly = true)
    public Page<EnderecoDTO> findAll(PageRequest pageRequest) {
        Page<Endereco> list = enderecoRepository.findAll(pageRequest);
        return list.map(EnderecoDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public EnderecoDTO findById(Long id) {
//        return  enderecoRepository.findById(id).map(EnderecoDTO::fromEntity)
//                .orElseThrow(() -> new ConcurrentModificationException("Endereco não encontrado"));

        var endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException("Endereço não encontrado"));
        return EnderecoDTO.fromEntity(endereco);
    }

    @Transactional
    public EnderecoDTO save(EnderecoDTO enderecoDTO) {
        Endereco entity = EnderecoDTO.toEntity(enderecoDTO);
        var enderecoSaved = enderecoRepository.save(entity);
        return EnderecoDTO.fromEntity(enderecoSaved);
    }

    @Transactional
    public EnderecoDTO update(Long id, EnderecoDTO enderecoDTO) {

        try {
            Endereco endereco = enderecoRepository.getReferenceById(id);
            EnderecoDTO.mapperDtoToEntity(enderecoDTO, endereco);
            endereco = enderecoRepository.save(endereco);
            return EnderecoDTO.fromEntity(endereco);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Endereço não encontrado, id: " + id);
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
