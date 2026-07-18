# Oracle Wallet

Coloca aquí los archivos del wallet que descargaste desde Oracle Cloud
(Autonomous Database > Connection > Download wallet):

- cwallet.sso
- ewallet.p12
- ewallet.pem (si aplica)
- keystore.jks
- ojdbc.properties
- sqlnet.ora
- tnsnames.ora
- truststore.jks

Esta carpeta está excluida de Git (ver `.gitignore` en la raíz) porque
contiene credenciales. `docker-compose.yml` la monta como volumen de solo
lectura dentro de ambos contenedores, en `/app/wallet`.

Si corres los microservicios directo en Windows (sin Docker), en cambio
se usa por defecto `C:/dev/despacho-guias/wallet` (puedes cambiarlo
seteando la variable de entorno `TNS_ADMIN_PATH`).
