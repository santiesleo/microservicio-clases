package co.analisys.clases.service.interfaces;

import co.analisys.clases.model.Clase;

import java.util.List;

public interface IClaseService {

    Clase programarClase(Clase clase);

    List<Clase> obtenerTodasClases();
}
