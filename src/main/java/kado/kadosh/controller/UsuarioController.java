package kado.kadosh.controller;

import kado.kadosh.dto.PersonaUpdateDTO;
import kado.kadosh.entities.Persona;
import kado.kadosh.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kado.kadosh.dto.ClienteSearchRequest;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ✅ Listar solo usuarios con rol CLIENTE (de nuevos a antiguos)
    @GetMapping("/clientes")
    public ResponseEntity<?> listarClientes() {
        var data = usuarioService.listarClientesOrdenados();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "total", data.size(),
                "data", data
        ));
    }

    // ===========================================================
    // ✅ Actualizar datos de Persona de un Usuario específico
    // ===========================================================
    @PutMapping("/{id}/persona")
    public ResponseEntity<?> actualizarPersona(
            @PathVariable String id,
            @RequestBody PersonaUpdateDTO dto
    ) {
        // ✅ Acepta UUID con o sin guiones (implementación local)
        UUID uuid = parseFlexibleUUID(id);

        Persona actualizada = usuarioService.updatePersonaByUsuarioId(uuid, dto);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Datos personales actualizados correctamente.",
                "data", actualizada
        ));
    }

    @PostMapping("/clientes/search")
    public ResponseEntity<?> buscarClientes(@RequestBody ClienteSearchRequest body) {
        boolean sinFiltros = (body.getDni() == null || body.getDni().isBlank())
                && (body.getNombre() == null || body.getNombre().isBlank());
        if (sinFiltros) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Envía al menos uno de los filtros: 'dni' o 'nombre'."
            ));
        }

        var data = usuarioService.buscarClientes(body);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "total", data.size(),
                "data", data
        ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String id) {
        try {

            UUID uuid = parseFlexibleUUID(id);

            usuarioService.eliminarUsuario(uuid);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "success", true,
                            "message", "Usuario eliminado correctamente"
                    ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al eliminar usuario: " + e.getMessage()
            ));
        }
    }

    // -----------------------
    // Util: parse flexible UUID
    // - acepta con guiones, sin guiones (32 hex), con prefijos "urn:uuid:" o "uuid:", o con llaves {}
    // - lanza IllegalArgumentException si no es válido
    // -----------------------
    private UUID parseFlexibleUUID(String raw) {
        if (raw == null) throw new IllegalArgumentException("ID de usuario nulo.");
        String s = raw.trim();

        // eliminar prefijos comunes
        if (s.toLowerCase().startsWith("urn:uuid:")) {
            s = s.substring(9);
        } else if (s.toLowerCase().startsWith("uuid:")) {
            s = s.substring(5);
        }

        // quitar llaves si existen
        if (s.startsWith("{") && s.endsWith("}")) {
            s = s.substring(1, s.length() - 1);
        }

        // Si ya tiene guiones, usar UUID.fromString directamente
        if (s.contains("-")) {
            return UUID.fromString(s);
        }

        // Si tiene 32 caracteres hex sin guiones, insertar guiones en las posiciones correctas
        if (s.length() == 32) {
            String dashed = s.substring(0, 8) + "-" +
                    s.substring(8, 12) + "-" +
                    s.substring(12, 16) + "-" +
                    s.substring(16, 20) + "-" +
                    s.substring(20);
            return UUID.fromString(dashed);
        }

        throw new IllegalArgumentException("Formato de UUID inválido: " + raw);
    }

}
