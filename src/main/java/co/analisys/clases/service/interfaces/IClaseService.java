package co.analisys.clases.service.interfaces;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.model.Clase;

import java.util.List;

public interface IClaseService {

    ClaseDTO programarClase(ClaseDTO claseDTO);

    List<ClaseDTO> obtenerTodasClases();
}
