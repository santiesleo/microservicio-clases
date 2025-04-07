package co.analisys.clases.service.impl;

import co.analisys.clases.dto.OcupacionClase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OcupacionClaseProducer {

    private final KafkaTemplate<String, OcupacionClase> kafkaTemplate;


    public OcupacionClaseProducer(KafkaTemplate<String, OcupacionClase> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void actualizarOcupacion(String claseId, int ocupacionActual) {
        OcupacionClase ocupacion = new OcupacionClase(claseId, ocupacionActual, LocalDateTime.now());
        System.out.println("Enviando mensaje a Kafka: " + ocupacion);
        kafkaTemplate.send("ocupacion-clases", claseId, ocupacion);
    }

}
