package co.analisys.clases.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clase {
    @EmbeddedId
    private ClaseId id;
    private String nombre;
    private LocalDateTime horario;
    private int capacidadMaxima;

    @Embedded
    private EntrenadorId entrenadorId;
}
