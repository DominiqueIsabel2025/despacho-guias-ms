# Despacho de Guías — EFT CDY2204

Sistema de gestión de guías de despacho, separado en **dos microservicios
independientes** (corrección del error señalado en la retroalimentación
de la Sumativa 3).

## Arquitectura

```
despacho-guias-productor/    Expone TODOS los endpoints REST.
                              Publica un mensaje en RabbitMQ al crear una guía.
                              Puerto 8080.

despacho-guias-consumidor/   No expone endpoints REST.
                              Escucha la cola, valida y persiste en Oracle.
                              Si falla la validación, el mensaje va a la DLQ.
                              Puerto 8081.
```

Cada carpeta es un **proyecto Maven independiente** (su propio `pom.xml`,
su propia clase `main`, su propio `Dockerfile`) — se compilan, dockerizan
y despliegan por separado en AWS EC2.

## Checklist de estado

- [x] Fase 0-5 (heredadas del repo original): estructura, Oracle, RabbitMQ
      (cola + DLQ), S3, endpoints REST, Spring Security, Azure AD B2C
- [x] Fase 6: separación en dos microservicios independientes
- [x] Fase 6b: corrección del `issuer-uri` de Azure AD B2C (faltaba)
- [x] Fase 6c: `S3Config` con perfil EC2 usando rol IAM (evita expiración
      de credenciales de AWS Academy Learner Lab)
- [x] Fase 7a: conexión a Oracle desde Docker corregida (wallet montado como
      volumen de solo lectura + `TNS_ADMIN_PATH` por variable de entorno,
      en vez de ruta hardcodeada de Windows)
- [ ] Fase 7b: falta build/push de las imágenes a Docker Hub y despliegue en EC2
- [ ] Fase 8: registrar cada endpoint como ruta independiente en AWS API Gateway
- [ ] Fase 9: pipeline CI/CD con GitHub Actions hacia EC2
- [ ] Fase 10: documento Word con el paso a paso (usando la plantilla oficial)
- [ ] Fase 11: video explicativo (máx. 15 min, orden: IDaaS → API Gateway →
      pruebas vía Gateway → evidencia en vivo de S3 → evidencia en vivo de RabbitMQ)

## Cómo correr todo en local

1. Copia los archivos de tu Oracle Wallet dentro de la carpeta `wallet/`
   (ver `wallet/README.md` para el detalle de qué archivos van ahí)
2. Levanta todo:

```bash
docker compose up --build
```

Esto levanta RabbitMQ, el productor (puerto 8080) y el consumidor (puerto 8081),
montando el wallet dentro de ambos contenedores automáticamente.

## Antes de correr: configurar los application.properties

Cada microservicio trae un `application.properties.example` en
`src/main/resources/`. Cópialo como `application.properties` (queda
ignorado por Git) y completa los valores marcados como
`TU_..._AQUI`, incluyendo el `issuer-uri` de Azure AD B2C.
