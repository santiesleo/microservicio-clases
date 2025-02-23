package co.analisys.clases.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaseDTO {
    private String id;
    private String nombre;
    private LocalDateTime horario;
    private int capacidadMaxima;
    private String entrenadorId;
}
