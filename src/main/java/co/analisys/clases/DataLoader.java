package co.analisys.clases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {

        System.out.println("Datos de prueba cargados exitosamente.");

    }
}