package kado.kadosh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialOpticoDTO {

    private UUID historialOpticoId;
    private UUID usuarioId;


    private LocalDateTime fecha;
    private String telefono;
    private String paciente;
    private Integer edad;


    private VisionDTO visionLejos;
    private VisionDTO visionCerca;


    private String imagenRefraccionUrl;
    private String imagenLenzometriaUrl;
    private String imagenKeratometriaUrl;


    private String recomendaciones;
    private String evaluador;

    private LocalDateTime createdAt;
}
