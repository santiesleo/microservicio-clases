package co.analisys.clases.dto;

import co.analisys.clases.model.ClaseId;
import co.analisys.clases.model.EntrenadorId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaseDTO {
    private ClaseId id;
    private String nombre;
    private LocalDateTime horario;
    private int capacidadMaxima;
    private EntrenadorId entrenadorId;
}
