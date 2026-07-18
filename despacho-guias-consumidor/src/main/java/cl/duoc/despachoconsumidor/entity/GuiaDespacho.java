package cl.duoc.despachoconsumidor.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Misma tabla que usa el productor (GUIA_DESPACHO en Oracle). Cada
// microservicio tiene su propia copia de la entidad porque son proyectos
// Maven independientes (no comparten codigo) - ambos apuntan a la misma
// base de datos Oracle Cloud.
@Entity
@Table(name = "GUIA_DESPACHO")
public class GuiaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMERO_GUIA", nullable = false, length = 50)
    private String numeroGuia;

    @Column(name = "TRANSPORTISTA", nullable = false, length = 100)
    private String transportista;

    @Column(name = "FECHA_DESPACHO", nullable = false)
    private LocalDate fechaDespacho;

    @Column(name = "DESTINO", length = 200)
    private String destino;

    @Column(name = "ESTADO", length = 30)
    private String estado;

    @Column(name = "URL_ARCHIVO_S3", length = 500)
    private String urlArchivoS3;

    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    public GuiaDespacho() {
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PROCESADA";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }

    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }

    public LocalDate getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDate fechaDespacho) { this.fechaDespacho = fechaDespacho; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUrlArchivoS3() { return urlArchivoS3; }
    public void setUrlArchivoS3(String urlArchivoS3) { this.urlArchivoS3 = urlArchivoS3; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
