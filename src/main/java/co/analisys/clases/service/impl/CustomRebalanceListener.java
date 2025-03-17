package co.analisys.clases.service.impl;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class CustomRebalanceListener implements ConsumerAwareRebalanceListener {

    private final RecuperacionService recuperacionService;

    public CustomRebalanceListener(RecuperacionService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("Particiones revocadas, guardando offsets...");
        // Aquí podrías guardar los offsets en la BD antes de perder la partición
    }


    public void onPartitionsAssigned(Collection<TopicPartition> partitions, Consumer<?, ?> consumer) {
        System.out.println("Particiones asignadas, cargando últimos offsets...");

        Map<TopicPartition, Long> offsets = recuperacionService.cargarUltimoOffset();
        for (TopicPartition partition : partitions) {
            Long offset = offsets.get(partition);
            if (offset != null) {
                System.out.println("Asignando offset " + offset + " a la partición " + partition);
                consumer.seek(partition, offset + 1);
            }
        }
    }
}
