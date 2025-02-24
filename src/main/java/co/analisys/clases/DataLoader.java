package co.analisys.clases;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.service.interfaces.IClaseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    private final IClaseService claseService;

    public DataLoader(IClaseService claseService) {
        this.claseService = claseService;
    }

    @Override
    public void run(String... args) {

        ClaseDTO clase1 = new ClaseDTO(UUID.randomUUID().toString(),
                "Yoga Matutino",
                LocalDateTime.now().plusDays(1).withHour(8).withMinute(0),
                20,
                "U001");

        ClaseDTO clase2 = new ClaseDTO(UUID.randomUUID().toString(),
                "Spinning Vespertino",
                LocalDateTime.now().plusDays(1).withHour(8).withMinute(0),
                20,
                "U002");

        claseService.programarClase(clase1);
        claseService.programarClase(clase2);

        System.out.println("Datos de prueba cargados exitosamente.");

    }
}