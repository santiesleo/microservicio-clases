package co.analisys.clases.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.Set;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Horario {
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Set<DiaSemana> diasSemana;

    public static Horario crear(LocalTime horaInicio, LocalTime horaFin, Set<DiaSemana> diasSemana) {
        if (horaInicio.isAfter(horaFin)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La hora de inicio debe ser anterior a la hora de fin");
        }
        return new Horario(horaInicio, horaFin, diasSemana);
    }

    public enum DiaSemana {
        LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
    }
}
