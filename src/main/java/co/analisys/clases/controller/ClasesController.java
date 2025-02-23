package co.analisys.clases.controller;

import co.analisys.clases.model.Clase;
import co.analisys.clases.service.interfaces.IClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clases")
class ClasesController {
    @Autowired
    private IClaseService equipoService;

    @PostMapping
    public Clase programarClase(@RequestBody Clase clase) {
        return equipoService.programarClase(clase);
    }

    @GetMapping
    public List<Clase> obtenerTodasClases() {
        return equipoService.obtenerTodasClases();
    }
}