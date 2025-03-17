package co.analisys.clases.service.impl;

import co.analisys.clases.dto.ClaseDTO;
import co.analisys.clases.dto.OcupacionClase;
import co.analisys.clases.service.interfaces.IClaseService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.Acknowledgment;

@Service
public class OcupacionClaseConsumer {

    private final IClaseService claseService;
    private final RecuperacionService recuperacionService;

    public OcupacionClaseConsumer(IClaseService claseService, RecuperacionService recuperacionService) {
        this.claseService = claseService;
        this.recuperacionService = recuperacionService;
    }

    @KafkaListener(
            topics = "ocupacion-clases",
            groupId = "monitoreo-grupo",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumirActualizacionOcupacion(ConsumerRecord<String, OcupacionClase> record, Acknowledgment ack) {
        try {
            OcupacionClase ocupacion = record.value();
            System.out.println("Mensaje recibido: " + ocupacion);
            actualizarDashboard(ocupacion);

            // Guardar el offset procesado
            recuperacionService.guardarOffset(record.topic(), record.partition(), record.offset());

            ack.acknowledge(); // confirmar procesamiento
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
            // No confirmamos el offset para que el mensaje pueda ser reintentado
        }
    }

    private void actualizarDashboard(OcupacionClase ocupacion) {
        ClaseDTO clase = claseService.obtenerClasePorId(ocupacion.getClaseId());
        if (clase != null) {
            System.out.println("Actualizando ocupación de la clase: " + ocupacion.getClaseId() + " a " + ocupacion.getOcupacionActual());
            clase.setOcupacionActual(ocupacion.getOcupacionActual());
            claseService.actualizarClase(clase);
        } else System.out.println("Clase no encontrada: " + ocupacion.getClaseId());
    }
}

