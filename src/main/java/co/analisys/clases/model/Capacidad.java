package co.analisys.clases.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Capacidad {
    private int valorMaximo;
    private int ocupacionActual;

    public Capacidad(int valorMaximo) {
        if (valorMaximo <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor que cero");
        }
        this.valorMaximo = valorMaximo;
        this.ocupacionActual = 0;
    }
}
