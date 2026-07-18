package cl.duoc.despachoproductor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.s3.region}")
    private String region;

    // ===================== Perfil LOCAL (desarrollo) =====================
    // Usa credenciales de sesion temporal de AWS Academy Learner Lab.
    // OJO: estas credenciales expiran cada ~4 horas, por eso SOLO se usan
    // en desarrollo local (perfil "local"), nunca en la instancia EC2.
    @Bean
    @Profile("local")
    public S3Client s3ClientLocal(
            @Value("${aws.accessKeyId}") String accessKeyId,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.sessionToken}") String sessionToken
    ) {
        AwsSessionCredentials credentials = AwsSessionCredentials.create(
                accessKeyId, secretKey, sessionToken
        );
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    // ===================== Perfil EC2 / produccion =====================
    // Usa el rol IAM asignado a la instancia EC2 (Instance Profile).
    // No hay access key ni secret key hardcodeados: las credenciales se
    // obtienen automaticamente desde el metadata service de la instancia
    // y AWS las rota solo. Esto evita el problema de las credenciales de
    // Learner Lab expirando en medio de una demo o del video.
    @Bean
    @Profile("!local")
    public S3Client s3ClientEc2() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
