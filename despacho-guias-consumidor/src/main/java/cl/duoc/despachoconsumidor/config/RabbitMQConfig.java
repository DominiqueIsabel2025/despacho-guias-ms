package cl.duoc.despachoconsumidor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Misma topologia que declara el productor (nombres identicos de queue/
// exchange/routing key). Declarar la misma topologia desde ambos lados es
// intencional y seguro: la declaracion en RabbitMQ es idempotente. Asi el
// consumidor tambien puede arrancar solo, sin depender de que el productor
// haya sido desplegado primero.
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PRINCIPAL = "guiaDespachoQueue";
    public static final String QUEUE_ERROR = "guiaDespachoErrorQueue";

    public static final String EXCHANGE_PRINCIPAL = "guiaDespachoExchange";
    public static final String EXCHANGE_ERROR = "guiaDespachoErrorExchange";

    public static final String ROUTING_KEY_PRINCIPAL = "guiaDespacho.routingkey";
    public static final String ROUTING_KEY_ERROR = "guiaDespacho.error.routingkey";

    @Bean
    public Queue colaPrincipal() {
        return QueueBuilder.durable(QUEUE_PRINCIPAL)
                .withArgument("x-dead-letter-exchange", EXCHANGE_ERROR)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_ERROR)
                .build();
    }

    @Bean
    public DirectExchange exchangePrincipal() {
        return new DirectExchange(EXCHANGE_PRINCIPAL);
    }

    @Bean
    public Binding bindingPrincipal() {
        return BindingBuilder.bind(colaPrincipal())
                .to(exchangePrincipal())
                .with(ROUTING_KEY_PRINCIPAL);
    }

    @Bean
    public Queue colaError() {
        return QueueBuilder.durable(QUEUE_ERROR).build();
    }

    @Bean
    public DirectExchange exchangeError() {
        return new DirectExchange(EXCHANGE_ERROR);
    }

    @Bean
    public Binding bindingError() {
        return BindingBuilder.bind(colaError())
                .to(exchangeError())
                .with(ROUTING_KEY_ERROR);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
