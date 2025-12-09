package kado.kadosh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaUpdateDTO {
    private String nombre;
    private String apellido;
    private Integer edad;
    private String email;
    private String telefono;
    private String imagenUrl;
    private String dni;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaNacimiento;
}
