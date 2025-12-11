# Configuración AWS - Segunda Entrega

Todos los servicios AWS que necesitas configurar paso a paso.

---

## 🎯 Lo que vas a crear

1. **RDS MySQL** - Base de datos relacional
2. **S3 Bucket** - Almacenar fotos de perfil
3. **SNS Topic** - Enviar notificaciones por email
4. **DynamoDB Table** - Gestión de sesiones

---

## 1️⃣ RDS MySQL

### Crear Base de Datos

1. AWS Console → **RDS** → **Create database**
2. Configuración:
   - **Engine**: MySQL 8.0.x
   - **Template**: Free tier
   - **DB identifier**: `aws-academy-db`
   - **Username**: `admin`
   - **Password**: `AwsAcademy2024!` (guárdala)
   - **Instance**: db.t3.micro
   - **Storage**: 20 GiB
   - **Public access**: ✅ **Yes** (importante)
   - **Database name**: `awsacademy`
   - **Backup**: 0 days
3. **Create database** (espera 5-10 min)

### Configurar Security Group

1. **EC2** → **Security Groups** → Selecciona el SG de RDS
2. **Edit inbound rules** → **Add rule**:
   - Type: MySQL/Aurora
   - Port: 3306
   - Source: 0.0.0.0/0
3. **Save rules**

### Obtener Endpoint

1. **RDS** → **Databases** → `aws-academy-db`
2. Copia el **Endpoint** (ej: `aws-academy-db.xxxxx.us-east-1.rds.amazonaws.com`)

### Crear Tablas (Opcional - Hibernate las crea)

Si prefieres crearlas manualmente:

```sql
USE awsacademy;

CREATE TABLE alumno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    matricula VARCHAR(50) NOT NULL UNIQUE,
    promedio DOUBLE NOT NULL,
    password VARCHAR(255),
    foto_perfil_url VARCHAR(500),
    INDEX (matricula)
);

CREATE TABLE profesor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_empleado VARCHAR(50) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    horas_clase INT NOT NULL,
    INDEX (numero_empleado)
);
```

---

## 2️⃣ S3 Bucket

### Crear Bucket

1. AWS Console → **S3** → **Create bucket**
2. **Bucket name**: `aws-academy-alumnos-fotos-TU-NOMBRE` (debe ser único)
3. **Region**: US East (N. Virginia) us-east-1
4. **Object Ownership**: ✅ ACLs enabled → Object writer
5. **Block Public Access**: ❌ Desactivar TODO (marca el checkbox de confirmación)
6. **Versioning**: Disable
7. **Encryption**: Disable
8. **Create bucket**

### Configurar Bucket Policy

1. Click en tu bucket → **Permissions** → **Bucket policy** → **Edit**
2. Pega esto (reemplaza `TU-NOMBRE-BUCKET`):

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublicReadGetObject",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::TU-NOMBRE-BUCKET/*"
        }
    ]
}
```

3. **Save changes**

---

## 3️⃣ SNS Topic

### Crear Topic

1. AWS Console → **SNS** → **Topics** → **Create topic**
2. **Type**: Standard
3. **Name**: `alumnos-notificaciones`
4. **Create topic**
5. Copia el **ARN** (ej: `arn:aws:sns:us-east-1:123456789:alumnos-notificaciones`)

### Suscribirte al Topic

1. En el topic → **Create subscription**
2. **Protocol**: Email
3. **Endpoint**: Tu correo de UADY
4. **Create subscription**
5. **Revisa tu email y confirma la suscripción** (busca en spam si no llega)

---

## 4️⃣ DynamoDB Table

### Crear Tabla

1. AWS Console → **DynamoDB** → **Tables** → **Create table**
2. **Table name**: `sesiones-alumnos`
3. **Partition key**: `id` (String)
4. **Sort key**: `alumnoId` (Number)
5. **Table settings**: Default settings
6. **Billing mode**: On-demand
7. **Create table** (espera 1-2 min)

---

## 5️⃣ Crear IAM Role para EC2 (Recomendado)

Esto evita usar credenciales temporales que expiran.

### Crear el Role

1. AWS Console → **IAM** → **Roles** → **Create role**
2. **Trusted entity type**: AWS service
3. **Use case**: EC2 → **Next**
4. **Add permissions** (busca y selecciona):
   - `AmazonS3FullAccess`
   - `AmazonSNSFullAccess`
   - `AmazonDynamoDBFullAccess`
   - `AmazonRDSDataFullAccess` (o solo lectura si prefieres)
5. **Role name**: `EC2-AWS-Academy-Role`
6. **Create role**

### Asignar el Role a EC2

1. **EC2** → **Instances** → Selecciona tu instancia
2. **Actions** → **Security** → **Modify IAM role**
3. **IAM role**: Selecciona `EC2-AWS-Academy-Role`
4. **Update IAM role**

✅ Con esto, **NO necesitas configurar** `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, ni `AWS_SESSION_TOKEN` en EC2.

---

## 5️⃣-B Obtener Credenciales AWS (Solo para desarrollo local)

Si desarrollas en tu computadora local (no en EC2):

1. En AWS Academy Learner Lab → **AWS Details**
2. Click en **Show** junto a AWS CLI
3. Copia:
   - `aws_access_key_id`
   - `aws_secret_access_key`
   - `aws_session_token`

⚠️ **Importante**: Estas credenciales **expiran** cuando detienes el lab. Deberás renovarlas cada sesión.

---

## 6️⃣ Configurar tu Aplicación

### A. Desarrollo Local (tu computadora)

Edita el archivo `.env` en la raíz del proyecto:

```properties
# Database
DATABASE_URL=jdbc:mysql://TU-RDS-ENDPOINT:3306/awsacademy?useSSL=false&serverTimezone=UTC
DATABASE_USERNAME=admin
DATABASE_PASSWORD=AwsAcademy2024!

# AWS Credentials (obtén estas de AWS Details)
AWS_ACCESS_KEY_ID=ASIA...
AWS_SECRET_ACCESS_KEY=...
AWS_SESSION_TOKEN=IQoJb3...

# AWS Configuration
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=aws-academy-alumnos-fotos-TU-NOMBRE
AWS_SNS_TOPIC_ARN=arn:aws:sns:us-east-1:123456789:alumnos-notificaciones
AWS_DYNAMODB_TABLE_NAME=sesiones-alumnos
```

⚠️ **Importante**: El archivo `.env` ya está en `.gitignore`, NO lo subas a Git.

### B. Producción en EC2 (con IAM Role)

Si tu EC2 tiene el IAM Role asignado, **NO necesitas** configurar credenciales AWS.

Crea un archivo `.env` en el servidor solo con:

```properties
# Database
DATABASE_URL=jdbc:mysql://TU-RDS-ENDPOINT:3306/awsacademy?useSSL=false&serverTimezone=UTC
DATABASE_USERNAME=admin
DATABASE_PASSWORD=AwsAcademy2024!

# AWS Configuration (sin credenciales - se usan del IAM Role)
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=aws-academy-alumnos-fotos-TU-NOMBRE
AWS_SNS_TOPIC_ARN=arn:aws:sns:us-east-1:123456789:alumnos-notificaciones
AWS_DYNAMODB_TABLE_NAME=sesiones-alumnos
```

El `application.properties` ya está configurado para leer variables de entorno automáticamente.

---

## 7️⃣ Compilar y Ejecutar

### En Local (Windows PowerShell)

```powershell
# Descargar dependencias
mvn clean install

# Cargar variables de entorno desde .env y ejecutar
Get-Content .env | ForEach-Object {
    if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        [Environment]::SetEnvironmentVariable($name, $value, "Process")
    }
}
mvn spring-boot:run
```

### En Local (Linux/Mac)

**Opción 1: Usar el script helper** (Recomendado)
```bash
# Descargar dependencias (solo primera vez)
mvn clean install

# Dar permisos de ejecución y ejecutar
chmod +x run-local.sh
./run-local.sh
```

**Opción 2: Manual**
```bash
# Descargar dependencias
mvn clean install

# Cargar variables y ejecutar
export $(cat .env | grep -v '^#' | xargs) && mvn spring-boot:run
```

### En EC2 (Con IAM Role)

```bash
# Compilar
mvn clean package -DskipTests

# Cargar variables desde .env y ejecutar JAR
export $(cat .env | grep -v '^#' | xargs)
java -jar target/aws-project-0.0.1-SNAPSHOT.jar
```

---

## 8️⃣ Probar Endpoints

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
Guarda el `id` que retorna.

### Subir Foto
```bash
curl -X POST http://localhost:8080/alumnos/1/fotoPerfil \
  -F "file=@C:/ruta/a/foto.jpg"
```

### Login
```bash
curl -X POST http://localhost:8080/alumnos/1/session/login \
  -H "Content-Type: application/json" \
  -d '{"password":"miPassword123"}'
```
Guarda el `sessionString`.

### Verificar Sesión
```bash
curl -X POST http://localhost:8080/alumnos/1/session/verify \
  -H "Content-Type: application/json" \
  -d '{"sessionString":"tu-session-string-aqui"}'
```
Debe retornar **200 OK**.

### Logout
```bash
curl -X POST http://localhost:8080/alumnos/1/session/logout \
  -H "Content-Type: application/json" \
  -d '{"sessionString":"tu-session-string-aqui"}'
```

### Enviar Email
```bash
curl -X POST http://localhost:8080/alumnos/1/email
```
Recibirás un email con la información del alumno.

---

## 🔧 Solución de Problemas

| Problema | Solución |
|----------|----------|
| No conecta a RDS | Verifica Security Group (puerto 3306 abierto) y Public access = Yes |
| Access Denied S3 | Verifica que ACLs estén habilitados y Block Public Access desactivado |
| SNS no envía email | Confirma tu suscripción desde el email que recibiste |
| DynamoDB error | Verifica que la tabla esté en estado "Active" |
| Credenciales expiradas | Ve a AWS Details → Show y copia nuevas credenciales |

---

## ✅ Checklist Final

### Infraestructura AWS
- [ ] RDS creado y disponible
- [ ] Security Group configurado (puerto 3306)
- [ ] S3 bucket creado con permisos públicos
- [ ] SNS topic creado
- [ ] Email suscrito y confirmado al SNS topic
- [ ] DynamoDB table creada (sesiones-alumnos)
- [ ] IAM Role creado y asignado a EC2 (si usas EC2)

### Configuración Local
- [ ] Archivo `.env` creado con todas las variables
- [ ] `.env` está en `.gitignore`
- [ ] Credenciales AWS actualizadas en `.env` (para local)

### Aplicación
- [ ] `mvn clean install` ejecutado sin errores
- [ ] Variables de entorno cargadas correctamente
- [ ] Aplicación inicia sin errores
- [ ] Todos los endpoints probados y funcionando

---

## 📝 Endpoints Completos

```
GET    /alumnos                      - Lista todos
GET    /alumnos/{id}                 - Obtiene uno
POST   /alumnos                      - Crea
PUT    /alumnos/{id}                 - Actualiza
DELETE /alumnos/{id}                 - Elimina
POST   /alumnos/{id}/fotoPerfil      - Sube foto a S3
POST   /alumnos/{id}/email           - Envía notificación SNS
POST   /alumnos/{id}/session/login   - Login (crea sesión en DynamoDB)
POST   /alumnos/{id}/session/verify  - Verifica sesión
POST   /alumnos/{id}/session/logout  - Logout (desactiva sesión)

GET    /profesores                   - Lista todos
GET    /profesores/{id}              - Obtiene uno
POST   /profesores                   - Crea
PUT    /profesores/{id}              - Actualiza
DELETE /profesores/{id}              - Elimina
```

¡Listo! Todo configurado para la segunda entrega.

