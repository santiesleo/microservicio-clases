package co.analisys.clases.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue inscripcionesQueue() {
        return new Queue("inscripciones.queue", true);
    }

    @Bean
    public Queue pagosQueue() {
        return QueueBuilder.durable("pagos-queue")
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "pagos-dlq")
                .withArgument("x-message-ttl", 30000) // 30 segundos
                .build();
    }

    @Bean
    public Queue pagosDLQ() {
        return QueueBuilder.durable("pagos-dlq").build();
    }

    @Bean
    public TopicExchange gimnasioExchange() {
        return new TopicExchange("gimnasio.exchange");
    }

    @Bean
    public Binding bindingInscripciones(Queue inscripcionesQueue, TopicExchange gimnasioExchange) {
        return BindingBuilder.bind(inscripcionesQueue).to(gimnasioExchange).with("inscripcion.nueva");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
