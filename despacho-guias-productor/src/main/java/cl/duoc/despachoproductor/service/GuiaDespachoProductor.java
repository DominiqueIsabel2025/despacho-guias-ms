package cl.duoc.despachoproductor.service;

import cl.duoc.despachoproductor.config.RabbitMQConfig;
import cl.duoc.despachoproductor.entity.GuiaDespachoMensaje;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Unica responsabilidad: publicar el mensaje en la cola principal.
// Este componente vive DENTRO del microservicio productor (proceso,
// JVM y despliegue independientes del microservicio consumidor).
@Component
public class GuiaDespachoProductor {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void enviarGuia(GuiaDespachoMensaje mensaje) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_PRINCIPAL,
                RabbitMQConfig.ROUTING_KEY_PRINCIPAL,
                mensaje
        );
    }
}
