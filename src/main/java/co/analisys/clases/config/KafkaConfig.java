package co.analisys.clases.config;

import co.analisys.clases.dto.ClaseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, ClaseDTO> kafkaTemplate(ProducerFactory<String, ClaseDTO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
