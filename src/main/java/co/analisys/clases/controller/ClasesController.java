package co.analisys.clases.controller;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.dto.OcupacionClase;
import co.analisys.clases.dto.OcupacionRequest;
import co.analisys.clases.service.interfaces.IClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/clases")
@Tag(name = "Clases", description = "Gestión de clases en el gimnasio")
public class ClasesController {
    private final IClaseService claseService;

    public ClasesController(IClaseService claseService) {
        this.claseService = claseService;
    }

    @Operation(summary = "Programar una nueva clase",
            description = "Permite programar una nueva clase en el sistema. " +
                    "Accesible solo para entrenadores y administradores.")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @PostMapping
    public ClaseDTO programarClase(@RequestBody ClaseDTO claseDTO) {
        return claseService.programarClase(claseDTO);
    }

    @Operation(summary = "Obtener todas las clases",
            description = "Permite obtener el listado de todas las clases programadas. " +
                    "Accesible para todos los usuarios registrados.")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER', 'ROLE_MEMBER')")
    @GetMapping
    public List<ClaseDTO> obtenerTodasClases() {
        return claseService.obtenerTodasClases();
    }

    @PutMapping("/{id}/ocupacion")
    public ResponseEntity<String> actualizarOcupacion(@PathVariable String id, @RequestBody ClaseDTO ocupacion) {
        ClaseDTO clase = claseService.obtenerClasePorId(id);
        if (clase == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clase no encontrada");
        }
        System.out.println("Actualizando ocupación de la clase: " + ocupacion.getId() + " original " +  clase.getOcupacionActual());
        clase.setOcupacionActual(ocupacion.getOcupacionActual());
        System.out.println("Actualizando ocupación de la clase: " + ocupacion.getId() + " a " + clase.getOcupacionActual());
        claseService.actualizarClase(clase);
        return ResponseEntity.ok("Ocupación actualizada");
    }
}
