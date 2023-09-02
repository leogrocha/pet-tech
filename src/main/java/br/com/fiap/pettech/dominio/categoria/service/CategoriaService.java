package br.com.fiap.pettech.dominio.categoria.service;

import br.com.fiap.pettech.dominio.categoria.dto.CategoriaDTO;
import br.com.fiap.pettech.dominio.categoria.entity.Categoria;
import br.com.fiap.pettech.dominio.categoria.repository.ICategoriaRepository;
import br.com.fiap.pettech.exception.service.ControllerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    @Autowired
    public ICategoriaRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoriaDTO> findAll(PageRequest pageRequest) {
        Page<Categoria> list = repository.findAll(pageRequest);
        return list.map(CategoriaDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id) {
        return repository.findById(id).map(CategoriaDTO::new).orElseThrow(() -> new ControllerNotFoundException("Categoria não encontrada"));
    }

    @Transactional
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        Categoria categoria = repository.save(new Categoria(categoriaDTO));
        return new CategoriaDTO(categoria);
    }

    @Transactional
    public CategoriaDTO update(Long id, CategoriaDTO categoriaDTO) {

            Categoria categoria = repository.findById(id)
                    .orElseThrow(() -> new ControllerNotFoundException("Recurso não encontrado"));
            categoriaDTO.setId(id);
            categoria.atualizarInformacoes(categoriaDTO);

            repository.save(categoria);

            return new CategoriaDTO(categoria);
    }

    @Transactional
    public void delete(Long id) {

        try {
            repository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw  new ControllerNotFoundException("Categoria não encontrada: " + id);
        } catch (DataIntegrityViolationException e) {
            throw  new ControllerNotFoundException("Integridade referêncial violada!");
        }
    }
}
