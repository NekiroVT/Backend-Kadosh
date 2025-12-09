package kado.kadosh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubDatosUserDTO {
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;
    private String imagenUrl;


}
