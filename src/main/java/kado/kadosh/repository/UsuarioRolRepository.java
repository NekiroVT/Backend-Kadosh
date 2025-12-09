package kado.kadosh.repository;

import kado.kadosh.entities.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UUID> {

    boolean existsByUsuario_UsuarioIdAndRol_RolId(UUID usuarioId, UUID rolId);

    Optional<UsuarioRol> findByUsuario_UsuarioIdAndRol_RolId(UUID usuarioId, UUID rolId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    Integer deleteByUsuario_UsuarioIdAndRol_RolId(UUID usuarioId, UUID rolId);



    List<UsuarioRol> findByUsuario_UsuarioId(UUID usuarioId);

    Optional<UsuarioRol> findFirstByUsuario_UsuarioIdOrderByCreatedAtDesc(UUID usuarioId);
    void deleteByUsuarioUsuarioId(UUID usuarioId);
}
