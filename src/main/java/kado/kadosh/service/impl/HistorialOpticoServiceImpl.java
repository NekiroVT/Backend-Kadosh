package kado.kadosh.service.impl;

import kado.kadosh.dto.HistorialOpticoDTO;
import kado.kadosh.dto.VisionDTO;
import kado.kadosh.entities.HistorialOptico;
import kado.kadosh.entities.Usuario;
import kado.kadosh.entities.Vision;
import kado.kadosh.repository.HistorialOpticoRepository;
import kado.kadosh.repository.VisionRepository;
import kado.kadosh.service.HistorialOpticoService;
import kado.kadosh.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HistorialOpticoServiceImpl implements HistorialOpticoService {

    @Autowired
    private HistorialOpticoRepository historialOpticoRepository;

    @Autowired
    private VisionRepository visionRepository;

    @Autowired
    private UsuarioService usuarioService;

    // ========================
    // 🟢 CREAR HISTORIAL
    // ========================
    @Override
    public HistorialOptico crearHistorial(HistorialOpticoDTO dto) {

        // ========================
        // 1) Resolver usuario dueño (si viene usuarioId)
        // ========================
        Usuario usuario = null;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioService.findById(dto.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));
        }

        // ========================
        // 2) VISIÓN LEJOS
        // ========================
        Vision visionLejos = new Vision();
        visionLejos.setVisionId(UUID.randomUUID());
        visionLejos.setTipoVision("lejos");

        if (dto.getVisionLejos() != null) {
            visionLejos.setOjoIzquierdoEsf(dto.getVisionLejos().getOjoIzquierdoEsf());
            visionLejos.setOjoDerechoEsf(dto.getVisionLejos().getOjoDerechoEsf());
            visionLejos.setOjoIzquierdoCil(dto.getVisionLejos().getOjoIzquierdoCil());
            visionLejos.setOjoDerechoCil(dto.getVisionLejos().getOjoDerechoCil());
            visionLejos.setOjoIzquierdoEje(dto.getVisionLejos().getOjoIzquierdoEje());
            visionLejos.setOjoDerechoEje(dto.getVisionLejos().getOjoDerechoEje());
            visionLejos.setOjoIzquierdoDip(dto.getVisionLejos().getOjoIzquierdoDip());
            visionLejos.setOjoDerechoDip(dto.getVisionLejos().getOjoDerechoDip());
            visionLejos.setOjoIzquierdoAv(dto.getVisionLejos().getOjoIzquierdoAv());
            visionLejos.setOjoDerechoAv(dto.getVisionLejos().getOjoDerechoAv());
        }

        visionLejos = visionRepository.save(visionLejos);

        // ========================
        // 3) VISIÓN CERCA
        // ========================
        Vision visionCerca = new Vision();
        visionCerca.setVisionId(UUID.randomUUID());
        visionCerca.setTipoVision("cerca");

        if (dto.getVisionCerca() != null) {
            visionCerca.setOjoIzquierdoEsf(dto.getVisionCerca().getOjoIzquierdoEsf());
            visionCerca.setOjoDerechoEsf(dto.getVisionCerca().getOjoDerechoEsf());
            visionCerca.setOjoIzquierdoCil(dto.getVisionCerca().getOjoIzquierdoCil());
            visionCerca.setOjoDerechoCil(dto.getVisionCerca().getOjoDerechoCil());
            visionCerca.setOjoIzquierdoEje(dto.getVisionCerca().getOjoIzquierdoEje());
            visionCerca.setOjoDerechoEje(dto.getVisionCerca().getOjoDerechoEje());
            visionCerca.setOjoIzquierdoDip(dto.getVisionCerca().getOjoIzquierdoDip());
            visionCerca.setOjoDerechoDip(dto.getVisionCerca().getOjoDerechoDip());
            visionCerca.setOjoIzquierdoAv(dto.getVisionCerca().getOjoIzquierdoAv());
            visionCerca.setOjoDerechoAv(dto.getVisionCerca().getOjoDerechoAv());
        }

        visionCerca = visionRepository.save(visionCerca);

        // ========================
        // 4) HISTORIAL ÓPTICO
        // ========================
        HistorialOptico historial = new HistorialOptico();
        historial.setHistorialOpticoId(UUID.randomUUID());
        historial.setFecha(dto.getFecha());
        historial.setTelefono(dto.getTelefono());
        historial.setPaciente(dto.getPaciente());
        historial.setEdad(dto.getEdad());
        historial.setVisionLejos(visionLejos);
        historial.setVisionCerca(visionCerca);
        historial.setRecomendaciones(dto.getRecomendaciones());
        historial.setEvaluador(dto.getEvaluador());

        historial.setImagenRefraccionUrl(dto.getImagenRefraccionUrl());
        historial.setImagenLenzometriaUrl(dto.getImagenLenzometriaUrl());
        historial.setImagenKeratometriaUrl(dto.getImagenKeratometriaUrl());

        // 👇 Vincular dueño del historial (usuario)
        if (usuario != null) {
            historial.setUsuario(usuario); // ajusta el nombre del setter si tu campo se llama distinto
        }

        return historialOpticoRepository.save(historial);
    }

    // ========================
    // 🟡 ACTUALIZAR HISTORIAL
    // ========================
    @Override
    public HistorialOptico actualizarHistorial(UUID id, HistorialOpticoDTO dto) {

        HistorialOptico historial = historialOpticoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial óptico no encontrado con id: " + id));

        // ========================
        // 1) Cambiar usuario dueño (opcional)
        // ========================
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioService.findById(dto.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));
            historial.setUsuario(usuario); // ajusta si el campo tiene otro nombre
        }

        // ========================
        // 2) Datos básicos
        // ========================
        if (dto.getFecha() != null) historial.setFecha(dto.getFecha());
        if (dto.getTelefono() != null) historial.setTelefono(dto.getTelefono());
        if (dto.getPaciente() != null) historial.setPaciente(dto.getPaciente());
        if (dto.getEdad() != null) historial.setEdad(dto.getEdad());
        if (dto.getRecomendaciones() != null) historial.setRecomendaciones(dto.getRecomendaciones());
        if (dto.getEvaluador() != null) historial.setEvaluador(dto.getEvaluador());

        // ========================
        // 3) URLs de imágenes
        // ========================
        if (dto.getImagenRefraccionUrl() != null)
            historial.setImagenRefraccionUrl(dto.getImagenRefraccionUrl());

        if (dto.getImagenLenzometriaUrl() != null)
            historial.setImagenLenzometriaUrl(dto.getImagenLenzometriaUrl());

        if (dto.getImagenKeratometriaUrl() != null)
            historial.setImagenKeratometriaUrl(dto.getImagenKeratometriaUrl());

        // ========================
        // 4) VISIÓN LEJOS
        // ========================
        Vision visionLejos = historial.getVisionLejos();
        if (visionLejos == null) {
            visionLejos = new Vision();
            visionLejos.setVisionId(UUID.randomUUID());
            visionLejos.setTipoVision("lejos");
        }

        if (dto.getVisionLejos() != null) {
            visionLejos.setOjoIzquierdoEsf(dto.getVisionLejos().getOjoIzquierdoEsf());
            visionLejos.setOjoDerechoEsf(dto.getVisionLejos().getOjoDerechoEsf());
            visionLejos.setOjoIzquierdoCil(dto.getVisionLejos().getOjoIzquierdoCil());
            visionLejos.setOjoDerechoCil(dto.getVisionLejos().getOjoDerechoCil());
            visionLejos.setOjoIzquierdoEje(dto.getVisionLejos().getOjoIzquierdoEje());
            visionLejos.setOjoDerechoEje(dto.getVisionLejos().getOjoDerechoEje());
            visionLejos.setOjoIzquierdoDip(dto.getVisionLejos().getOjoIzquierdoDip());
            visionLejos.setOjoDerechoDip(dto.getVisionLejos().getOjoDerechoDip());
            visionLejos.setOjoIzquierdoAv(dto.getVisionLejos().getOjoIzquierdoAv());
            visionLejos.setOjoDerechoAv(dto.getVisionLejos().getOjoDerechoAv());
        }

        visionLejos = visionRepository.save(visionLejos);
        historial.setVisionLejos(visionLejos);

        // ========================
        // 5) VISIÓN CERCA
        // ========================
        Vision visionCerca = historial.getVisionCerca();
        if (visionCerca == null) {
            visionCerca = new Vision();
            visionCerca.setVisionId(UUID.randomUUID());
            visionCerca.setTipoVision("cerca");
        }

        if (dto.getVisionCerca() != null) {
            visionCerca.setOjoIzquierdoEsf(dto.getVisionCerca().getOjoIzquierdoEsf());
            visionCerca.setOjoDerechoEsf(dto.getVisionCerca().getOjoDerechoEsf());
            visionCerca.setOjoIzquierdoCil(dto.getVisionCerca().getOjoIzquierdoCil());
            visionCerca.setOjoDerechoCil(dto.getVisionCerca().getOjoDerechoCil());
            visionCerca.setOjoIzquierdoEje(dto.getVisionCerca().getOjoIzquierdoEje());
            visionCerca.setOjoDerechoEje(dto.getVisionCerca().getOjoDerechoEje());
            visionCerca.setOjoIzquierdoDip(dto.getVisionCerca().getOjoIzquierdoDip());
            visionCerca.setOjoDerechoDip(dto.getVisionCerca().getOjoDerechoDip());
            visionCerca.setOjoIzquierdoAv(dto.getVisionCerca().getOjoIzquierdoAv());
            visionCerca.setOjoDerechoAv(dto.getVisionCerca().getOjoDerechoAv());
        }

        visionCerca = visionRepository.save(visionCerca);
        historial.setVisionCerca(visionCerca);

        // ========================
        // 6) Guardar y devolver
        // ========================
        return historialOpticoRepository.save(historial);
    }

    // ========================
    // 🔁 MAPPER ENTITY → DTO
    // ========================
    private HistorialOpticoDTO toDTO(HistorialOptico historial) {
        if (historial == null) return null;

        HistorialOpticoDTO dto = new HistorialOpticoDTO();

        dto.setHistorialOpticoId(historial.getHistorialOpticoId());
        dto.setFecha(historial.getFecha());
        dto.setTelefono(historial.getTelefono());
        dto.setPaciente(historial.getPaciente());
        dto.setEdad(historial.getEdad());
        dto.setRecomendaciones(historial.getRecomendaciones());
        dto.setEvaluador(historial.getEvaluador());
        dto.setImagenRefraccionUrl(historial.getImagenRefraccionUrl());
        dto.setImagenLenzometriaUrl(historial.getImagenLenzometriaUrl());
        dto.setImagenKeratometriaUrl(historial.getImagenKeratometriaUrl());
        dto.setCreatedAt(historial.getCreatedAt()); // si tu entidad lo tiene

        // usuarioId (dueño)
        if (historial.getUsuario() != null) {
            dto.setUsuarioId(historial.getUsuario().getUsuarioId()); // ajusta si se llama distinto
        }

        // ===== VISIÓN LEJOS =====
        if (historial.getVisionLejos() != null) {
            Vision v = historial.getVisionLejos();
            VisionDTO vdto = new VisionDTO();
            vdto.setOjoIzquierdoEsf(v.getOjoIzquierdoEsf());
            vdto.setOjoDerechoEsf(v.getOjoDerechoEsf());
            vdto.setOjoIzquierdoCil(v.getOjoIzquierdoCil());
            vdto.setOjoDerechoCil(v.getOjoDerechoCil());
            vdto.setOjoIzquierdoEje(v.getOjoIzquierdoEje());
            vdto.setOjoDerechoEje(v.getOjoDerechoEje());
            vdto.setOjoIzquierdoDip(v.getOjoIzquierdoDip());
            vdto.setOjoDerechoDip(v.getOjoDerechoDip());
            vdto.setOjoIzquierdoAv(v.getOjoIzquierdoAv());
            vdto.setOjoDerechoAv(v.getOjoDerechoAv());
            dto.setVisionLejos(vdto);
        }

        // ===== VISIÓN CERCA =====
        if (historial.getVisionCerca() != null) {
            Vision v = historial.getVisionCerca();
            VisionDTO vdto = new VisionDTO();
            vdto.setOjoIzquierdoEsf(v.getOjoIzquierdoEsf());
            vdto.setOjoDerechoEsf(v.getOjoDerechoEsf());
            vdto.setOjoIzquierdoCil(v.getOjoIzquierdoCil());
            vdto.setOjoDerechoCil(v.getOjoDerechoCil());
            vdto.setOjoIzquierdoEje(v.getOjoIzquierdoEje());
            vdto.setOjoDerechoEje(v.getOjoDerechoEje());
            vdto.setOjoIzquierdoDip(v.getOjoIzquierdoDip());
            vdto.setOjoDerechoDip(v.getOjoDerechoDip());
            vdto.setOjoIzquierdoAv(v.getOjoIzquierdoAv());
            vdto.setOjoDerechoAv(v.getOjoDerechoAv());
            dto.setVisionCerca(vdto);
        }

        return dto;
    }
}
