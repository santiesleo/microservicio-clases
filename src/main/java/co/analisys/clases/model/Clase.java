package co.analisys.clases.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clase {
    @EmbeddedId
    private ClaseId id;
    private String nombre;
    @Embedded
    private Horario horario;
    @Embedded
    private Capacidad capacidadMaxima;

    @Embedded
    private EntrenadorId entrenadorId;
}
