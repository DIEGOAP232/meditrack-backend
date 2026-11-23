# 🏥 MediTrack Backend

### Plataforma de servicios REST para la gestión de atención médica

**MediTrack Backend** es el componente de servidor de la plataforma MediTrack. Proporciona servicios REST para gestionar procesos relacionados con usuarios, pacientes, autenticación, citas médicas y vinculación entre pacientes y tutores.

Está desarrollado con **Java y Quarkus**, utiliza **SQL Server** como sistema de gestión de base de datos y emplea autenticación basada en tokens JWT para proteger los recursos de la aplicación.

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Quarkus-3.40.1.1-4695EB?style=for-the-badge&logo=quarkus&logoColor=white" alt="Quarkus"/>
  <img src="https://img.shields.io/badge/SQL_Server-2022-CC2927?style=for-the-badge&logo=microsoftsqlserver&logoColor=white" alt="SQL Server"/>
  <img src="https://img.shields.io/badge/API-REST-55D6BE?style=for-the-badge" alt="REST API"/>
</p>

---

## 📌 1. Descripción general

MediTrack es una solución tecnológica orientada a facilitar la gestión y el seguimiento de la atención médica de los pacientes.

El backend constituye la capa central de procesamiento del sistema. Recibe solicitudes de la aplicación móvil, ejecuta las reglas de negocio, valida los permisos de acceso y gestiona las operaciones sobre la base de datos.

Su diseño separa las responsabilidades de exposición de servicios, lógica de negocio y acceso a datos, favoreciendo la organización y el mantenimiento del código.

## 🎯 2. Objetivos del backend

- Proporcionar servicios REST para la aplicación móvil MediTrack.
- Gestionar la autenticación y autorización de usuarios.
- Procesar el registro de pacientes.
- Administrar las operaciones relacionadas con las citas médicas.
- Gestionar la vinculación entre pacientes y tutores.
- Consultar información de especialidades, médicos y servicios.
- Proteger los recursos mediante autenticación JWT.
- Mantener la comunicación con SQL Server mediante JDBC.

## 🏗️ 3. Arquitectura del sistema

MediTrack utiliza una arquitectura de tres componentes independientes:

```text
             ┌─────────────────────────┐
             │    MEDITrack Android    │
             │       Frontend          │
             │        Java             │
             └────────────┬────────────┘
                          │
                     HTTP / JSON
                          │
                          ▼
             ┌─────────────────────────┐
             │    MEDITrack Backend    │
             │       Quarkus           │
             │       REST API          │
             └────────────┬────────────┘
                          │
                     JDBC / SQL
                          │
                          ▼
             ┌─────────────────────────┐
             │   MEDITrack Database    │
             │       SQL Server        │
             └─────────────────────────┘
```

### Arquitectura interna

El backend organiza el procesamiento de solicitudes mediante tres capas principales:

| Capa | Responsabilidad |
|---|---|
| Resources | Exposición de endpoints y recepción de solicitudes HTTP. |
| Services | Validaciones y ejecución de reglas de negocio. |
| Repositories | Consultas y operaciones sobre SQL Server. |

También utiliza DTO para controlar los datos intercambiados y modelos para representar la información gestionada por la aplicación.

## ⚙️ 4. Tecnologías utilizadas

| Tecnología | Función |
|---|---|
| Java 21 | Lenguaje de programación. |
| Quarkus 3.40.1.1 | Framework backend. |
| Maven | Gestión de dependencias y compilación. |
| Microsoft SQL Server 2022 | Sistema de gestión de base de datos. |
| JDBC | Comunicación con SQL Server. |
| JWT | Autenticación basada en tokens. |
| REST / JSON | Intercambio de información. |
| Git / GitHub | Control de versiones. |

## 📂 5. Estructura del proyecto

```text
meditrack-backend/
│
├── src/
│   ├── main/
│   │   ├── java/com/meditrack/
│   │   │   │
│   │   │   ├── config/
│   │   │   │
│   │   │   ├── dto/
│   │   │   │
│   │   │   ├── model/
│   │   │   │
│   │   │   ├── repository/
│   │   │   │
│   │   │   ├── resources/
│   │   │   │
│   │   │   └── service/
│   │   │
│   │   ├── resources/
│   │   │   └── application.properties
│   │   │
│   │   └── docker/
│   │
│   └── test/
│       └── java/com/meditrack/
│
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── .gitignore
└── README.md
```

### Descripción de paquetes

- **config:** configuración de componentes del backend.
- **dto:** objetos utilizados para transportar información entre las capas.
- **model:** representación de las entidades y modelos de datos.
- **repository:** acceso a la base de datos mediante JDBC.
- **resources:** endpoints REST de la aplicación.
- **service:** lógica de negocio, autenticación, validaciones y gestión de operaciones.

## 🔐 6. Seguridad y autenticación

El backend utiliza JSON Web Token (JWT) para gestionar la autenticación de las solicitudes protegidas.

Entre sus componentes se encuentran:

- `AuthResource`: endpoints relacionados con la autenticación.
- `AuthService`: validación de credenciales y autenticación.
- `JwtService`: generación de tokens JWT.
- `PasswordService`: generación y verificación de hashes de contraseñas.
- `AuthPerfilResource`: acceso a información del perfil autenticado.

Los tokens permiten identificar al usuario y aplicar controles de acceso según sus roles.

La aplicación utiliza claves RSA para la firma y verificación de los tokens.

**Importante:** las claves privadas y las credenciales de conexión deben mantenerse fuera del repositorio.

## 🧩 7. Funcionalidades implementadas

El backend cuenta actualmente con los siguientes componentes:

| Componente | Funcionalidad |
|---|---|
| Autenticación | Inicio de sesión y generación de tokens JWT. |
| Usuarios | Consulta y gestión de información de usuarios. |
| Registro | Registro de pacientes. |
| Citas médicas | Registro y consulta de citas. |
| Especialidades | Consulta de especialidades médicas. |
| Médicos | Consulta de médicos por especialidad. |
| Servicios | Consulta de servicios disponibles. |
| Tutores | Vinculación y consulta de pacientes asociados. |
| Administración | Recursos relacionados con operaciones administrativas. |

La gestión de tratamientos, medicamentos y otros procesos clínicos se encuentra contemplada en la evolución funcional de MediTrack y no debe considerarse implementada en este backend hasta completar su desarrollo e integración.

## 🗄️ 8. Conexión con SQL Server

El backend utiliza Microsoft SQL Server como motor de persistencia.

La comunicación con la base de datos se realiza mediante JDBC y el componente `DataSource` de Quarkus.

La configuración se encuentra en:

```text
src/main/resources/application.properties
```

Parámetros principales:

```properties
quarkus.datasource.db-kind=mssql

quarkus.datasource.jdbc.url=jdbc:sqlserver://localhost:5333;databaseName=MediTrack;integratedSecurity=true;encrypt=true;trustServerCertificate=true;sendTimeAsDatetime=false
```

La configuración de conexión debe adaptarse al entorno donde se ejecute el backend.

Las claves RSA se mantienen fuera del repositorio y sus ubicaciones deben configurarse de acuerdo con el entorno de ejecución.

## 🚀 9. Instalación y ejecución

### Requisitos

- Java Development Kit (JDK) 21.
- Apache Maven o Maven Wrapper incluido.
- Microsoft SQL Server compatible con la configuración del proyecto.
- Claves RSA para la autenticación JWT.
- Git.

### Clonar el repositorio

```bash
git clone https://github.com/DIEGOAP232/meditrack-backend.git
```

Entrar al proyecto:

```bash
cd meditrack-backend
```

### Ejecutar en modo desarrollo

En Windows:

```cmd
mvnw.cmd quarkus:dev
```

En Linux o macOS:

```bash
./mvnw quarkus:dev
```

El servidor se inicia utilizando el puerto 8080, salvo que la configuración indique otro puerto.

### Consola de desarrollo

Durante la ejecución en modo desarrollo, Quarkus proporciona una interfaz de desarrollo:

```text
http://localhost:8080/q/dev/
```

### Compilar el proyecto

Windows:

```cmd
mvnw.cmd package
```

Linux o macOS:

```bash
./mvnw package
```

El artefacto ejecutable se genera en:

```text
target/quarkus-app/
```

Para ejecutar el paquete:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## 🧪 10. Pruebas

El proyecto incorpora pruebas automatizadas relacionadas con autenticación, usuarios, registro y citas médicas.

Para ejecutar las pruebas:

```cmd
mvnw.cmd test
```

En Linux o macOS:

```bash
./mvnw test
```

Las pruebas permiten validar el comportamiento de componentes y recursos del backend.

## 🔗 11. Repositorios relacionados

MediTrack se organiza mediante repositorios independientes:

| Componente | Repositorio |
|---|---|
| Aplicación Android | [meditrack-android](https://github.com/DIEGOAP232/meditrack-android) |
| Backend REST | [meditrack-backend](https://github.com/DIEGOAP232/meditrack-backend) |
| Base de datos | Repositorio independiente de documentación y scripts técnicos, sin datos clínicos reales. |

## 🔒 12. Seguridad y privacidad

Debido a que MediTrack contempla información relacionada con la salud, la protección de los datos constituye una consideración fundamental.

Se deben mantener las siguientes prácticas:

- No publicar contraseñas ni credenciales de conexión.
- No subir claves privadas RSA.
- No almacenar datos médicos reales en repositorios públicos.
- Validar la identidad y los permisos de los usuarios.
- Utilizar consultas SQL parametrizadas.
- Mantener la información sensible fuera del control de versiones.

## 📈 13. Estado del proyecto

MediTrack Backend se encuentra en desarrollo progresivo.

La implementación actual contempla autenticación, registro de pacientes, gestión de citas médicas y vinculación de tutores.

La arquitectura permite incorporar nuevos módulos y ampliar las funcionalidades conforme evolucione el sistema.

---

<p align="center">
  <strong>MediTrack</strong><br/>
  Tecnología para una gestión médica más organizada.
</p>
