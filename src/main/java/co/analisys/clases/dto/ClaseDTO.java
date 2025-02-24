package co.analisys.clases.dto;

import co.analisys.clases.model.Horario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {
    private String id;
    private String nombre;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Set<Horario.DiaSemana> diasSemana;
    private int capacidadMaxima;
    private int ocupacionActual;
    private String entrenadorId;
}
