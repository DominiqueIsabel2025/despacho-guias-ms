package cl.duoc.despachoconsumidor.entity;

import java.io.Serializable;
import java.time.LocalDate;

// Debe tener la MISMA forma (mismos nombres de campo) que el
// GuiaDespachoMensaje del productor, porque Jackson2JsonMessageConverter
// serializa/deserializa por nombre de campo, no por tipo compartido.
public class GuiaDespachoMensaje implements Serializable {

    private String numeroGuia;
    private String transportista;
    private LocalDate fechaDespacho;
    private String destino;
    private String urlArchivoS3;

    public GuiaDespachoMensaje() {
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
