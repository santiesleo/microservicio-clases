package co.analisys.clases.service.impl;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;
import co.analisys.clases.service.impl.RecuperacionService;

import java.util.Collection;
import java.util.Map;

@Component
public class CustomRebalanceListener implements ConsumerRebalanceListener {

    private final RecuperacionService recuperacionService;
    private Consumer<?, ?> consumer;

    public CustomRebalanceListener(RecuperacionService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }

    // Método para asignar el consumidor
    public void setConsumer(Consumer<?, ?> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("Particiones revocadas, guardando offsets...");
        // Aquí podrías guardar los offsets en la BD antes de perder la partición
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.println("Particiones asignadas, cargando últimos offsets...");

        Map<TopicPartition, Long> offsets = recuperacionService.cargarUltimoOffset();
        for (TopicPartition partition : partitions) {
            Long offset = offsets.get(partition);
            if (offset != null && consumer != null) {
                System.out.println("Asignando offset " + offset + " a la partición " + partition);
                consumer.seek(partition, offset + 1);  // Ahora consumer NO es null
            }
        }
    }
}
