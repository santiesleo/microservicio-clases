package co.analisys.clases.config;

import co.analisys.clases.dto.OcupacionClase;
import co.analisys.clases.service.impl.CustomRebalanceListener;
import co.analisys.clases.service.impl.RecuperacionService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private final RecuperacionService recuperacionService;
    private final CustomRebalanceListener rebalanceListener;

    public KafkaConfig(RecuperacionService recuperacionService, CustomRebalanceListener rebalanceListener) {
        this.recuperacionService = recuperacionService;
        this.rebalanceListener = rebalanceListener;
    }

    // 🔹 Configuración del Producer
    @Bean
    public ProducerFactory<String, OcupacionClase> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OcupacionClase> kafkaTemplate(ProducerFactory<String, OcupacionClase> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // 🔹 Configuración del Consumer
    @Bean
    public ConsumerFactory<String, OcupacionClase> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "monitoreo-grupo");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(OcupacionClase.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OcupacionClase> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OcupacionClase> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Configurar el rebalance listener en el factory
        factory.getContainerProperties().setConsumerRebalanceListener(rebalanceListener);

        // Configure manual acknowledgment if needed
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }

}
