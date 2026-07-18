package cl.duoc.despachoconsumidor.service;

import cl.duoc.despachoconsumidor.config.RabbitMQConfig;
import cl.duoc.despachoconsumidor.entity.GuiaDespacho;
import cl.duoc.despachoconsumidor.entity.GuiaDespachoMensaje;
import cl.duoc.despachoconsumidor.repository.GuiaDespachoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Unica responsabilidad: escuchar la cola principal, validar y persistir
// en Oracle. Este componente vive DENTRO del microservicio consumidor
// (proceso, JVM y despliegue independientes del microservicio productor).
@Component
public class GuiaDespachoConsumidor {

    @Autowired
    private GuiaDespachoRepository guiaDespachoRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PRINCIPAL)
    public void procesarGuia(GuiaDespachoMensaje mensaje) {

        // Validacion minima: si falta un dato obligatorio, se lanza
        // excepcion -> Spring AMQP rechaza el mensaje -> RabbitMQ lo
        // redirige automaticamente a la DLQ (guiaDespachoErrorQueue),
        // gracias al x-dead-letter-exchange configurado en la cola.
        if (mensaje.getNumeroGuia() == null || mensaje.getTransportista() == null) {
            throw new IllegalArgumentException(
                    "Mensaje invalido: numeroGuia y transportista son obligatorios. Va a la DLQ.");
        }

        GuiaDespacho guia = new GuiaDespacho();
        guia.setNumeroGuia(mensaje.getNumeroGuia());
        guia.setTransportista(mensaje.getTransportista());
        guia.setFechaDespacho(mensaje.getFechaDespacho());
        guia.setDestino(mensaje.getDestino());
        guia.setUrlArchivoS3(mensaje.getUrlArchivoS3());

        guiaDespachoRepository.save(guia);
    }

    // Listener de la DLQ: solo para dejar evidencia (logs) de que el
    // mecanismo de dead-letter funciona - util para mostrarlo en el video.
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ERROR)
    public void procesarError(Object mensajeFallido) {
        System.err.println("[DLQ] Mensaje recibido en la cola de errores: " + mensajeFallido);
    }
}
