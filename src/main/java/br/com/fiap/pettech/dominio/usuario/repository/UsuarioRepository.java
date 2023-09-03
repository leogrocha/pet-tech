package br.com.fiap.pettech.dominio.usuario.repository;

import br.com.fiap.pettech.dominio.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
