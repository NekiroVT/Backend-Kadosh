package kado.kadosh.repository;

import kado.kadosh.entities.HistorialOptico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistorialOpticoRepository extends JpaRepository<HistorialOptico, UUID> {
}
