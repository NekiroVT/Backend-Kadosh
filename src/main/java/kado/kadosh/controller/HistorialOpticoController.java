package kado.kadosh.controller;

import kado.kadosh.dto.HistorialOpticoDTO;
import kado.kadosh.dto.VisionDTO;
import kado.kadosh.entities.HistorialOptico;
import kado.kadosh.entities.Vision;
import kado.kadosh.service.HistorialOpticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/historial-optico")
public class HistorialOpticoController {

    @Autowired
    private HistorialOpticoService historialService;

    // ========================
    // 🟢 CREAR (POST)
    // ========================
    @PostMapping
    public ResponseEntity<HistorialOpticoDTO> crearHistorial(@RequestBody HistorialOpticoDTO dto) {

        HistorialOptico historial = historialService.crearHistorial(dto);

        HistorialOpticoDTO resp = toDTO(historial);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // ========================
    // 🟡 EDITAR (PUT)
    // ========================
    @PutMapping("/{id}")
    public ResponseEntity<HistorialOpticoDTO> actualizarHistorial(
            @PathVariable String id,
            @RequestBody HistorialOpticoDTO dto) {

        // SIN UUIDUtil → UUID puro
        UUID uuid = UUID.fromString(id);

        HistorialOptico actualizado = historialService.actualizarHistorial(uuid, dto);

        HistorialOpticoDTO resp = toDTO(actualizado);

        return ResponseEntity.ok(resp);
    }

    // ========================
    // MAPPER ENTITY → DTO
    // ========================
    private HistorialOpticoDTO toDTO(HistorialOptico historial) {
        if (historial == null) return null;

        HistorialOpticoDTO dto = new HistorialOpticoDTO();

        dto.setHistorialOpticoId(historial.getHistorialOpticoId());
        if (historial.getUsuario() != null) {
            dto.setUsuarioId(historial.getUsuario().getUsuarioId());
        }

        dto.setFecha(historial.getFecha());
        dto.setTelefono(historial.getTelefono());
        dto.setPaciente(historial.getPaciente());
        dto.setEdad(historial.getEdad());
        dto.setRecomendaciones(historial.getRecomendaciones());
        dto.setEvaluador(historial.getEvaluador());

        dto.setImagenRefraccionUrl(historial.getImagenRefraccionUrl());
        dto.setImagenLenzometriaUrl(historial.getImagenLenzometriaUrl());
        dto.setImagenKeratometriaUrl(historial.getImagenKeratometriaUrl());

        dto.setCreatedAt(historial.getCreatedAt());

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
