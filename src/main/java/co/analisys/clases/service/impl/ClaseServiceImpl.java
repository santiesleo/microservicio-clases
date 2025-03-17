package co.analisys.clases.service.impl;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.model.*;
import co.analisys.clases.repository.ClaseRepository;
import co.analisys.clases.service.interfaces.IClaseService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaseServiceImpl implements IClaseService {

    private final ClaseRepository claseRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    public ClaseServiceImpl(ClaseRepository claseRepository, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.claseRepository = claseRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ClaseDTO programarClase(ClaseDTO claseDTO) {

        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "http://localhost:8087/api/entrenadores/" + claseDTO.getEntrenadorId() + "/disponible",
                Boolean.class
        );

        if (Boolean.TRUE.equals(response.getBody())) {
            Clase clase = mapToEntity(claseDTO);
            Clase savedClase = claseRepository.save(clase);

            rabbitTemplate.convertAndSend("gimnasio.exchange", "horarios.cambio", "Horario cambiado: " + clase.getNombre());

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
    public void cambiarHorario(ClaseDTO claseDTO) {
        Clase clase = claseRepository.findById(new ClaseId(claseDTO.getId()))
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        // Actualizar horario
        clase.setHorario(Horario.crear(claseDTO.getHoraInicio(), claseDTO.getHoraFin(), claseDTO.getDiasSemana()));
        claseRepository.save(clase);

        // Enviar notificación a RabbitMQ
        rabbitTemplate.convertAndSend("gimnasio.exchange", "horarios.cambio", claseDTO);

        System.out.println("Horario cambiado y notificado en RabbitMQ: " + claseDTO.getNombre());
    }

}
