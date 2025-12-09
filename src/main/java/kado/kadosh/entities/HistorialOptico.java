package kado.kadosh.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historial_optico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialOptico {

    @Id
    @Column(name = "historial_optico_id", columnDefinition = "RAW(16)")
    private UUID historialOpticoId;



    // ===========================
    // 🔗 Relaciones indirectas
    // ===========================
    @ManyToOne
    @JoinColumn(
            name = "vision_lejos_id",
            referencedColumnName = "vision_id",
            foreignKey = @ForeignKey(name = "fk_historial_vision_lejos")
    )
    private Vision visionLejos;

    @ManyToOne
    @JoinColumn(
            name = "vision_cerca_id",
            referencedColumnName = "vision_id",
            foreignKey = @ForeignKey(name = "fk_historial_vision_cerca")
    )
    private Vision visionCerca;

    // ===========================
    // 🔗 Usuario propietario
    // ===========================
    @ManyToOne
    @JoinColumn(
            name = "usuario_id",
            referencedColumnName = "usuario_id",
            foreignKey = @ForeignKey(name = "fk_historial_usuario")
    )
    private Usuario usuario;                 // ✅ AGREGADO

    // ===========================
    // 📋 Datos del historial
    // ===========================
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 50)
    private String telefono;

    @Column(length = 100)
    private String paciente;

    @Column
    private Integer edad;

    @Column(length = 1000)
    private String recomendaciones;

    @Column(length = 100)
    private String evaluador;

    // ===========================
    // 🖼️ Imágenes
    // ===========================
    @Column(length = 300)
    private String imagenRefraccionUrl;

    @Column(length = 300)
    private String imagenLenzometriaUrl;

    @Column(length = 300)
    private String imagenKeratometriaUrl;



    // ===========================
    // ⏱ Auditoría
    // ===========================
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
