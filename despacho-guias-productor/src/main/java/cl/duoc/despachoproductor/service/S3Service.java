package cl.duoc.despachoproductor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String subirArchivo(MultipartFile archivo, String numeroGuia) throws IOException {
        String nombreArchivo = "guias/" + numeroGuia + "-" + UUID.randomUUID() + "-" + archivo.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(nombreArchivo)
                .contentType(archivo.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(archivo.getInputStream(), archivo.getSize()));

        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, nombreArchivo);
    }
}
