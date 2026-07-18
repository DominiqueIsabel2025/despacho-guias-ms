package cl.duoc.despachoproductor.entity;

import java.io.Serializable;
import java.time.LocalDate;

// DTO que viaja por la cola RabbitMQ. Se serializa a JSON automaticamente
// gracias al Jackson2JsonMessageConverter configurado en RabbitMQConfig.
public class GuiaDespachoMensaje implements Serializable {

    private String numeroGuia;
    private String transportista;
    private LocalDate fechaDespacho;
    private String destino;
    private String urlArchivoS3;

    public GuiaDespachoMensaje() {
    }

    public GuiaDespachoMensaje(String numeroGuia, String transportista, LocalDate fechaDespacho, String destino, String urlArchivoS3) {
        this.numeroGuia = numeroGuia;
        this.transportista = transportista;
        this.fechaDespacho = fechaDespacho;
        this.destino = destino;
        this.urlArchivoS3 = urlArchivoS3;
    }

    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }

    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }

    public LocalDate getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDate fechaDespacho) { this.fechaDespacho = fechaDespacho; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getUrlArchivoS3() { return urlArchivoS3; }
    public void setUrlArchivoS3(String urlArchivoS3) { this.urlArchivoS3 = urlArchivoS3; }
}
