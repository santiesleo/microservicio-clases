package co.analisys.clases.controller;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.service.interfaces.IClaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clases")
class ClasesController {
    private final IClaseService equipoService;

    public ClasesController(IClaseService equipoService) {
        this.equipoService = equipoService;
    }

    @PostMapping
    public ClaseDTO programarClase(@RequestBody ClaseDTO claseDTO) {
        System.out.println("Recibiendo petición para programar clase: " + claseDTO);
        return equipoService.programarClase(claseDTO);
    }

    @GetMapping
    public List<ClaseDTO> obtenerTodasClases() {
        System.out.println("Consultando todas las clases");
        return equipoService.obtenerTodasClases();
    }
}