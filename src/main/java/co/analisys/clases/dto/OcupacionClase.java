package co.analisys.clases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcupacionClase implements Serializable {
    private String claseId;
    private int ocupacionActual;
    private LocalDateTime timestamp;
}
