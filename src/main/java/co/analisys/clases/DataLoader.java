package co.analisys.clases;

import co.analisys.clases.model.Clase;
import co.analisys.clases.model.ClaseId;
import co.analisys.clases.model.EntrenadorId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {


    @Override
    public void run(String... args) {

        Clase clase1 = new Clase(new ClaseId(UUID.randomUUID().toString()),
                "Yoga Matutino",
                LocalDateTime.now().plusDays(1).withHour(8).withMinute(0),
                20,
                new EntrenadorId("U001"));

        Clase clase2 = new Clase(new ClaseId(UUID.randomUUID().toString()),
                "Spinning Vespertino",
                LocalDateTime.now().plusDays(1).withHour(8).withMinute(0),
                20,
                new EntrenadorId("U002"));

        System.out.println("Datos de prueba cargados exitosamente.");

    }
}