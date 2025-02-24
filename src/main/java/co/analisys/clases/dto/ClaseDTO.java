package co.analisys.clases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {
    private String id;
    private String nombre;
    private LocalDateTime horario;
    private int capacidadMaxima;
    private String entrenadorId;
}
