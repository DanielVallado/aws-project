# AWS Academy Foundations API - Quick Start

## 📚 Documentación Completa

- **[Configuración AWS Completa (RDS + S3)](docs/CONFIGURACION_AWS_COMPLETA.md)** - Guía paso a paso
- **[Setup AWS RDS](docs/AWS_RDS_SETUP.md)** - Detalles técnicos de RDS

## 🚀 Inicio Rápido

### 1. Instalar Dependencias

```bash
mvn clean install
```

### 2. Configurar Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto con:

```properties
# Database
DATABASE_URL=jdbc:mysql://TU-ENDPOINT-RDS:3306/awsacademy?useSSL=false&serverTimezone=UTC
DATABASE_USERNAME=admin
DATABASE_PASSWORD=AwsAcademy2024!

# AWS Credentials
AWS_ACCESS_KEY_ID=ASIA...
AWS_SECRET_ACCESS_KEY=...
AWS_SESSION_TOKEN=IQoJb3...

# AWS Configuration
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=tu-bucket-name
AWS_SNS_TOPIC_ARN=arn:aws:sns:us-east-1:...
AWS_DYNAMODB_TABLE_NAME=sesiones-alumnos
```

⚠️ El archivo `.env` está en `.gitignore` y no se subirá a Git.

### 3. Ejecutar Aplicación

**Windows:**
```powershell
.\run-local.ps1
```

**Linux/Mac:**
```bash
chmod +x run-local.sh
./run-local.sh
```

La API estará disponible en: `http://localhost:8080`

## 🔗 Endpoints Disponibles

### Alumnos

- `GET /alumnos` - Lista todos los alumnos
- `GET /alumnos/{id}` - Obtiene un alumno (incluye URL de foto)
- `POST /alumnos` - Crea un alumno
- `PUT /alumnos/{id}` - Actualiza un alumno
- `DELETE /alumnos/{id}` - Elimina un alumno
- `POST /alumnos/{id}/fotoPerfil` - Sube foto de perfil (multipart/form-data)

### Profesores

- `GET /profesores` - Lista todos los profesores
- `GET /profesores/{id}` - Obtiene un profesor
- `POST /profesores` - Crea un profesor
- `PUT /profesores/{id}` - Actualiza un profesor
- `DELETE /profesores/{id}` - Elimina un profesor

## 📝 Ejemplos de Uso

### Crear Alumno

```bash
curl -X POST http://localhost:8080/alumnos \
  -H "Content-Type: application/json" \
  -d '{
    "nombres": "Juan",
    "apellidos": "Pérez",
    "matricula": "A00123456",
    "promedio": 9.5,
    "password": "miPassword123"
  }'
```

### Subir Foto de Perfil

```bash
curl -X POST http://localhost:8080/alumnos/1/fotoPerfil \
  -F "file=@/ruta/a/foto.jpg"
```

### Consultar Alumno con Foto

```bash
curl http://localhost:8080/alumnos/1
```

Respuesta:
```json
{
  "id": 1,
  "nombres": "Juan",
  "apellidos": "Pérez",
  "matricula": "A00123456",
  "promedio": 9.5,
  "password": "miPassword123",
  "fotoPerfilUrl": "https://tu-bucket.s3.us-east-1.amazonaws.com/alumnos/uuid.jpg"
}
```

## 🛠️ Tecnologías

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL (AWS RDS)
- AWS SDK for S3
- Lombok
- Maven

## 📦 Estructura del Proyecto

```
src/main/java/com/uady/awsproject/
├── alumno/
│   ├── controller/     # REST controllers
│   ├── entity/         # JPA entities
│   ├── mapper/         # Entity ↔ Model mappers
│   ├── model/          # Domain models (DTOs)
│   ├── repository/     # Data access layer
│   └── service/        # Business logic
├── profesor/           # Same structure as alumno
└── common/
    ├── config/         # AWS S3 configuration
    ├── exception/      # Exception handling
    ├── service/        # Shared services (S3Service)
    └── util/           # Utilities
```

## 🔒 Seguridad

⚠️ **IMPORTANTE**: Este proyecto es para fines educativos.

- Las credenciales AWS están en `application.properties` (no recomendado para producción)
- El bucket S3 es público (solo para desarrollo)
- No hay autenticación ni autorización implementada

Para producción, considera:
- Usar AWS Secrets Manager o Parameter Store
- Implementar Spring Security
- Configurar políticas IAM más restrictivas
- Usar HTTPS

## 📖 Notas Adicionales

### Credenciales AWS Academy

Las credenciales AWS Academy **expiran** cuando se detiene el laboratorio. Deberás:

1. Ir a AWS Details
2. Copiar nuevas credenciales
3. Actualizar `application.properties`
4. Reiniciar la aplicación

### Base de Datos

El esquema de la base de datos se crea automáticamente gracias a:
```properties
spring.jpa.hibernate.ddl-auto=update
```

Si prefieres crear las tablas manualmente, consulta el script SQL en `docs/AWS_RDS_SETUP.md`.

## 🐛 Solución de Problemas

### Error: Cannot connect to RDS

✅ Verifica:
- Security Group permite puerto 3306
- Public access está habilitado
- Endpoint es correcto

### Error: Access Denied S3

✅ Verifica:
- Credenciales AWS están actualizadas
- Bucket tiene ACLs habilitados
- Block public access está desactivado

### Error: Cannot resolve symbol 'software'

✅ Ejecuta:
```bash
mvn clean install
```

## 📞 Soporte

Para más información, consulta la documentación completa en `docs/`.

