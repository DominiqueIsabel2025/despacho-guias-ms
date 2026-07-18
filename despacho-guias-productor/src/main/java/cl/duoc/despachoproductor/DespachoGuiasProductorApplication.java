package cl.duoc.despachoproductor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Punto de entrada del microservicio PRODUCTOR.
// Esta es una aplicacion Spring Boot completamente independiente:
// tiene su propio pom.xml, su propio proceso, su propio puerto,
// y se despliega en su propio contenedor Docker / instancia EC2.
@SpringBootApplication
public class DespachoGuiasProductorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DespachoGuiasProductorApplication.class, args);
    }
}
