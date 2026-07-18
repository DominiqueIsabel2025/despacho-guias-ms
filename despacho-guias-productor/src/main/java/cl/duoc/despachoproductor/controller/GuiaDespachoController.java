package cl.duoc.despachoproductor.controller;

import cl.duoc.despachoproductor.entity.GuiaDespacho;
import cl.duoc.despachoproductor.entity.GuiaDespachoMensaje;
import cl.duoc.despachoproductor.repository.GuiaDespachoRepository;
import cl.duoc.despachoproductor.service.GuiaDespachoProductor;
import cl.duoc.despachoproductor.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Todos los endpoints REST del sistema viven en el microservicio PRODUCTOR.
// Solo "crearGuia" interactua con la cola (publica un mensaje); el resto
// opera directo contra Oracle Cloud via el repository.
@RestController
@RequestMapping("/api/guias")
public class GuiaDespachoController {

    @Autowired
    private GuiaDespachoProductor guiaDespachoProductor;

    @Autowired
    private GuiaDespachoRepository guiaDespachoRepository;

    @Autowired
    private S3Service s3Service;

    // 1. Crear guia de despacho + subir archivo a S3
    // Flujo: sube el archivo a S3 -> publica mensaje en la cola RabbitMQ
    // -> el microservicio CONSUMIDOR (proceso aparte) guarda en Oracle.
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> crearGuia(
            @RequestParam("numeroGuia") String numeroGuia,
            @RequestParam("transportista") String transportista,
            @RequestParam("fechaDespacho") String fechaDespacho,
            @RequestParam("destino") String destino,
            @RequestParam("archivo") MultipartFile archivo
    ) throws IOException {

        String urlArchivo = s3Service.subirArchivo(archivo, numeroGuia);

        GuiaDespachoMensaje mensaje = new GuiaDespachoMensaje(
                numeroGuia,
                transportista,
                LocalDate.parse(fechaDespacho),
                destino,
                urlArchivo
        );

        guiaDespachoProductor.enviarGuia(mensaje);

        return ResponseEntity.ok("Guia enviada a procesamiento. Numero: " + numeroGuia + ". Archivo: " + urlArchivo);
    }

    // 2. Descargar guia (retorna la URL del archivo en S3)
    @GetMapping("/{id}/descargar")
    public ResponseEntity<String> descargarGuia(@PathVariable Long id) {
        Optional<GuiaDespacho> guia = guiaDespachoRepository.findById(id);
        if (guia.isPresent()) {
            return ResponseEntity.ok(guia.get().getUrlArchivoS3());
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Modificar/actualizar guia
    @PutMapping("/{id}")
    public ResponseEntity<GuiaDespacho> modificarGuia(@PathVariable Long id, @RequestBody GuiaDespacho datosActualizados) {
        Optional<GuiaDespacho> guiaExistente = guiaDespachoRepository.findById(id);
        if (guiaExistente.isPresent()) {
            GuiaDespacho guia = guiaExistente.get();
            guia.setTransportista(datosActualizados.getTransportista());
            guia.setFechaDespacho(datosActualizados.getFechaDespacho());
            guia.setDestino(datosActualizados.getDestino());
            guia.setEstado(datosActualizados.getEstado());
            return ResponseEntity.ok(guiaDespachoRepository.save(guia));
        }
        return ResponseEntity.notFound().build();
    }

    // 4. Eliminar guia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGuia(@PathVariable Long id) {
        if (guiaDespachoRepository.existsById(id)) {
            guiaDespachoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 5. Consultar por transportista y fecha
    @GetMapping("/buscar")
    public ResponseEntity<List<GuiaDespacho>> buscarPorTransportistaYFecha(
            @RequestParam String transportista,
            @RequestParam String fecha
    ) {
        List<GuiaDespacho> resultado = guiaDespachoRepository
                .findByTransportistaAndFechaDespacho(transportista, LocalDate.parse(fecha));
        return ResponseEntity.ok(resultado);
    }

    // 6. Listar todas (util para verificar en Postman durante el desarrollo)
    @GetMapping
    public List<GuiaDespacho> listarTodas() {
        return guiaDespachoRepository.findAll();
    }
}
