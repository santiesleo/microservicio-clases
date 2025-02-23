package co.analisys.clases.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;

import java.time.LocalDateTime;

public class Clase {
    @EmbeddedId
    private ClaseId id;
    private String nombre;
    private LocalDateTime horario;
    private int capacidadMaxima;

    @Embedded
    private EntrenadorId entrenadorId;
}
