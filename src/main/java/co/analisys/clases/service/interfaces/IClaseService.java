package co.analisys.clases.service.interfaces;

import co.analisys.clases.dto.ClaseDTO;

import java.util.List;

public interface IClaseService {

    ClaseDTO programarClase(ClaseDTO claseDTO);

    List<ClaseDTO> obtenerTodasClases();

<<<<<<< HEAD
    ClaseDTO obtenerClasePorId(String claseId);

    void actualizarClase(ClaseDTO claseDTO);


    void actualizarClaseSinMensaje(ClaseDTO clase);
=======
    void cambiarHorario(ClaseDTO claseDTO);

>>>>>>> origin/main
}
