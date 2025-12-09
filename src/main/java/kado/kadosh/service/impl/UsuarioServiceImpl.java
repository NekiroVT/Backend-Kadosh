package kado.kadosh.service.impl;

import kado.kadosh.dto.ClienteListItemDTO;
import kado.kadosh.dto.SubDatosUserDTO;
import kado.kadosh.dto.PersonaUpdateDTO;
import kado.kadosh.entities.Persona;
import kado.kadosh.entities.Usuario;
import kado.kadosh.repository.PersonaRepository;
import kado.kadosh.repository.UsuarioRepository;
import kado.kadosh.repository.UsuarioRolRepository;
import kado.kadosh.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kado.kadosh.dto.ClienteSearchRequest;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PersonaRepository personaRepository;

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByDni(String dni) {
        return usuarioRepository.findActivoByPersonaDni(dni);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByPersonaIdActivo(UUID personaId) {
        return usuarioRepository.findByPersonaIdActivo(personaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(UUID usuarioId) {
        return usuarioRepository.findById(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> rolUnicoPorUsuarioId(UUID usuarioId) {
        return usuarioRolRepository.findByUsuario_UsuarioId(usuarioId).stream()
                .filter(Objects::nonNull)
                .map(ur -> ur.getRol() != null ? ur.getRol().getNombre() : null)
                .filter(r -> r != null && !r.isBlank())
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubDatosUserDTO> subdatosPorUsuarioId(UUID usuarioId) {
        return usuarioRepository.findById(usuarioId).map(u -> {
            String nombre     = u.getPersona() != null ? u.getPersona().getNombre()     : null;
            String apellido   = u.getPersona() != null ? u.getPersona().getApellido()   : null;
            String correo     = u.getPersona() != null ? u.getPersona().getEmail()      : null;
            String imagenUrl  = u.getPersona() != null ? u.getPersona().getImagenUrl()  : null;
            String rol        = rolUnicoPorUsuarioId(usuarioId).orElse(null);
            return new SubDatosUserDTO(nombre, apellido, correo, rol, imagenUrl);
        });
    }

    // ===========================================================
    // ✅ Actualizar datos de Persona vía usuarioId
    //     Si viene fechaNacimiento → recalculamos edad
    // ===========================================================
    @Override
    public Persona updatePersonaByUsuarioId(UUID usuarioId, PersonaUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findByIdWithPersona(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        Persona p = usuario.getPersona();
        if (p == null) {
            throw new IllegalStateException("El usuario no tiene persona asociada: " + usuarioId);
        }

        if (dto.getNombre()    != null) p.setNombre(dto.getNombre().trim());
        if (dto.getApellido()  != null) p.setApellido(dto.getApellido().trim());
        if (dto.getEmail()     != null) p.setEmail(dto.getEmail().trim());
        if (dto.getTelefono()  != null) p.setTelefono(dto.getTelefono().trim());
        if (dto.getImagenUrl() != null) p.setImagenUrl(dto.getImagenUrl().trim());
        if (dto.getDni()       != null) p.setDni(dto.getDni().trim());


        if (dto.getFechaNacimiento() != null) {
            p.setFechaNacimiento(dto.getFechaNacimiento());
        }

        if (p.getFechaNacimiento() != null) {

            p.setEdad(calcularEdad(p.getFechaNacimiento()));
        } else if (dto.getEdad() != null) {

            p.setEdad(dto.getEdad());
        }

        return personaRepository.save(p);
    }

    // ===========================================================
    // Listar clientes por rol y fecha DESC
    // ===========================================================
    @Override
    @Transactional(readOnly = true)
    public List<ClienteListItemDTO> listarClientesOrdenados() {
        List<Usuario> usuarios = usuarioRepository.findByRolNombreOrderByCreatedAtDesc("Cliente");

        return usuarios.stream().map(u -> {
            Persona p = u.getPersona();
            return new ClienteListItemDTO(
                    u.getUsuarioId(),
                    p != null ? p.getNombre() : null,
                    p != null ? p.getApellido() : null,
                    p != null ? p.getEdad() : null,
                    p != null ? p.getEmail() : null,
                    p != null ? p.getTelefono() : null,
                    p != null ? p.getDni() : null,
                    p != null ? p.getImagenUrl() : null,
                    p != null ? p.getFechaNacimiento() : null,
                    u.getCreatedAt()
            );
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteListItemDTO> buscarClientes(ClienteSearchRequest req) {
        String dni = (req.getDni() == null || req.getDni().isBlank()) ? null : req.getDni().trim();
        String nombre = (req.getNombre() == null || req.getNombre().isBlank()) ? null : req.getNombre().trim();

        if (dni == null && nombre == null) {
            return List.of();
        }

        List<Usuario> usuarios = usuarioRepository.searchClientesFlexible(dni, nombre, "Cliente");

        return usuarios.stream().map(u -> {
            Persona p = u.getPersona();
            return new ClienteListItemDTO(
                    u.getUsuarioId(),
                    p != null ? p.getNombre() : null,
                    p != null ? p.getApellido() : null,
                    p != null ? p.getEdad() : null,
                    p != null ? p.getEmail() : null,
                    p != null ? p.getTelefono() : null,
                    p != null ? p.getDni() : null,
                    p != null ? p.getImagenUrl() : null,
                    p != null ? p.getFechaNacimiento() : null,
                    u.getCreatedAt()
            );
        }).toList();
    }

    @Override
    @Transactional
    public void eliminarUsuario(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con el ID proporcionado."));

        usuarioRolRepository.deleteByUsuarioUsuarioId(usuarioId);
        usuarioRepository.delete(usuario);
    }

    // ==========================
    // Helper para edad
    // ==========================
    private Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return null;
        LocalDate hoy = LocalDate.now();
        if (fechaNacimiento.isAfter(hoy)) {
            return 0;
        }
        return Period.between(fechaNacimiento, hoy).getYears();
    }
}
