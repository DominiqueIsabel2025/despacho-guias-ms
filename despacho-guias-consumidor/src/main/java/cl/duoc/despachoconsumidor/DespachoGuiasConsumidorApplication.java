package cl.duoc.despachoconsumidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Punto de entrada del microservicio CONSUMIDOR. Aplicacion Spring Boot
// independiente del productor: proceso propio, pom.xml propio, se
// despliega en su propio contenedor Docker / instancia EC2. No expone
// endpoints REST de negocio, solo escucha RabbitMQ.
@SpringBootApplication
public class DespachoGuiasConsumidorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DespachoGuiasConsumidorApplication.class, args);
    }
}
