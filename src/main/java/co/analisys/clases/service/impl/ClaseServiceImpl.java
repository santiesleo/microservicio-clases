package co.analisys.clases.service.impl;

import co.analisys.clases.model.Clase;
import co.analisys.clases.repository.ClaseRepository;
import co.analisys.clases.service.interfaces.IClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseServiceImpl implements IClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Override
    public Clase programarClase(Clase clase) {
        return claseRepository.save(clase);
    }

    @Override
    public List<Clase> obtenerTodasClases() {
        return claseRepository.findAll();
    }
}
