package br.com.fiap.pettech.dominio.usuario.service;

import br.com.fiap.pettech.dominio.pessoa.dto.PessoaDTO;
import br.com.fiap.pettech.dominio.pessoa.dto.PessoaEnderecoDTO;
import br.com.fiap.pettech.dominio.pessoa.entity.Pessoa;
import br.com.fiap.pettech.dominio.usuario.dto.UsuarioDTO;
import br.com.fiap.pettech.dominio.usuario.dto.UsuarioPessoaDTO;
import br.com.fiap.pettech.dominio.usuario.entity.Usuario;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioPessoaDTO> findAll(PageRequest pageRequest) {
        Page<Usuario> list = usuarioRepository.findAll(pageRequest);
        return list.map(UsuarioPessoaDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public UsuarioPessoaDTO findById(Long id) {
        return usuarioRepository.findById(id).map(UsuarioPessoaDTO::fromEntity)
                .orElseThrow(() -> new ControllerNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario entity = UsuarioDTO.toEntity(usuarioDTO);
        var usuarioSaved = usuarioRepository.save(entity);
        return UsuarioDTO.fromEntity(usuarioSaved);
    }

    @Transactional
    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {

        try {
            Usuario usuario = usuarioRepository.getReferenceById(id);
            UsuarioDTO.mapperDtoToEntity(usuarioDTO, usuario);
            usuario = usuarioRepository.save(usuario);
            return UsuarioDTO.fromEntity(usuario);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Usuário não encontrado");
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade de dados");
        }
    }

}
