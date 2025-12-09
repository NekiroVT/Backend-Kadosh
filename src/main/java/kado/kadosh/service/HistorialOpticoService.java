package kado.kadosh.service;

import kado.kadosh.dto.HistorialOpticoDTO;
import kado.kadosh.entities.HistorialOptico;

import java.util.UUID;

public interface HistorialOpticoService {
    HistorialOptico crearHistorial(HistorialOpticoDTO dto);
    HistorialOptico actualizarHistorial(UUID id, HistorialOpticoDTO dto);
}
