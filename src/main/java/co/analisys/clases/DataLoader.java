package co.analisys.clases;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.model.Horario;
import co.analisys.clases.service.interfaces.IClaseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    private final IClaseService claseService;

    public DataLoader(IClaseService claseService) {
        this.claseService = claseService;
    }

    @Override
    public void run(String... args) {

        ClaseDTO clase1 = new ClaseDTO(
                UUID.randomUUID().toString(),
                "Yoga Matutino",
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                Set.of(Horario.DiaSemana.LUNES, Horario.DiaSemana.MIERCOLES, Horario.DiaSemana.VIERNES),
                20,
                5,
                "U001");

        ClaseDTO clase2 = new ClaseDTO(
                UUID.randomUUID().toString(),
                "Spinning Vespertino",
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                Set.of(Horario.DiaSemana.MARTES, Horario.DiaSemana.JUEVES),
                15,
                8,
                "U002");

        claseService.programarClase(clase1);
        claseService.programarClase(clase2);

        System.out.println("Datos de prueba cargados exitosamente.");

    }
}