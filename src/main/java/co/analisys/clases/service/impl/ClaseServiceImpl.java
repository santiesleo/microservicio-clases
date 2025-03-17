package co.analisys.clases.service.impl;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.model.*;
import co.analisys.clases.repository.ClaseRepository;
import co.analisys.clases.service.interfaces.IClaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClaseServiceImpl implements IClaseService {

    private final ClaseRepository claseRepository;
    private final RestTemplate restTemplate;
    private final OcupacionClaseProducer ocupacionClaseProducer;

    public ClaseServiceImpl(ClaseRepository claseRepository, RestTemplate restTemplate, OcupacionClaseProducer ocupacionClaseProducer) {
        this.claseRepository = claseRepository;
        this.restTemplate = restTemplate;
        this.ocupacionClaseProducer = ocupacionClaseProducer;
    }

    @Override
    public ClaseDTO programarClase(ClaseDTO claseDTO) {

        // Verificar disponibilidad del entrenador
        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "http://localhost:8087/api/entrenadores/" + claseDTO.getEntrenadorId() + "/disponible",
                Boolean.class
        );

        if (Boolean.TRUE.equals(response.getBody())) {
            Clase clase = mapToEntity(claseDTO);
            Clase savedClase = claseRepository.save(clase);
            return mapToDTO(savedClase);
        } else {
            throw new RuntimeException("El entrenador no está disponible");
        }
    }

    @Override
    public List<ClaseDTO> obtenerTodasClases() {
        return claseRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private Clase mapToEntity(ClaseDTO dto) {
        Horario horario = Horario.crear(dto.getHoraInicio(), dto.getHoraFin(), dto.getDiasSemana());
        Clase clase = new Clase();
        clase.setId(new ClaseId(dto.getId()));
        clase.setNombre(dto.getNombre());
        clase.setHorario(horario);
        clase.setCapacidadMaxima(new Capacidad(dto.getCapacidadMaxima()));
        clase.setEntrenadorId(new EntrenadorId(dto.getEntrenadorId()));
        return clase;
    }

    private ClaseDTO mapToDTO(Clase clase) {
        ClaseDTO dto = new ClaseDTO();
        dto.setId(clase.getId().getClaseIdValue());
        dto.setNombre(clase.getNombre());
        dto.setHoraInicio(clase.getHorario().getHoraInicio());
        dto.setHoraFin(clase.getHorario().getHoraFin());
        dto.setDiasSemana(clase.getHorario().getDiasSemana());
        dto.setCapacidadMaxima(clase.getCapacidadMaxima().getValorMaximo());
        dto.setEntrenadorId(clase.getEntrenadorId().getEntrenadorIdValue());
        return dto;
    }

    @Override
    public ClaseDTO obtenerClasePorId(String claseId) {
        return claseRepository.findById(new ClaseId(claseId))
                .map(this::mapToDTO)
                .orElseThrow(() ->  new RuntimeException("Clase no encontrada"));
    }

    @Override
    public void actualizarClase(ClaseDTO claseDTO) {
        System.out.println("Actualizando clase: " + claseDTO);

        Optional<Clase> claseOptional = claseRepository.findById(new ClaseId(claseDTO.getId()));
        if (claseOptional.isPresent()) {
            Clase clase = claseOptional.get();
            clase.setCapacidadMaxima(new Capacidad(claseDTO.getCapacidadMaxima(), claseDTO.getOcupacionActual()));

            claseRepository.save(clase);
            ocupacionClaseProducer.actualizarOcupacion(claseDTO.getId(), claseDTO.getOcupacionActual());
        } else {
            System.out.println("Clase no encontrada: " + claseDTO.getId());
        }
    }

    public void actualizarClaseSinMensaje(ClaseDTO claseDTO) {
        System.out.println("Actualizando clase sin enviar mensaje: " + claseDTO);

        Optional<Clase> claseOptional = claseRepository.findById(new ClaseId(claseDTO.getId()));
        if (claseOptional.isPresent()) {
            Clase clase = claseOptional.get();
            clase.setCapacidadMaxima(new Capacidad(claseDTO.getCapacidadMaxima(), claseDTO.getOcupacionActual()));
            claseRepository.save(clase);
            // No llamar a ocupacionClaseProducer.actualizarOcupacion()
        } else {
            System.out.println("Clase no encontrada: " + claseDTO.getId());
        }
    }

}
