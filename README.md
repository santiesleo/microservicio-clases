# 🏋️‍♂️ Taller de Comunicación y Seguridad en Microservicios

Este proyecto implementa un sistema de gestión para un gimnasio utilizando **Spring Boot**, **RabbitMQ**, **Keycloak** y **Docker**. La **Parte 2** del taller se enfoca en la **comunicación asincrónica con RabbitMQ** y la **gestión de pagos**.

---

## 🎯 Objetivo General

Implementar y demostrar la integración de seguridad, APIs RESTful documentadas y comunicación asincrónica en el proyecto de microservicios del gimnasio.

---

## 📝 Parte 2: Comunicación Asincrónica con RabbitMQ (Tu Contribución)

Esta sección detalla la implementación de la comunicación asincrónica utilizando RabbitMQ, incluyendo la configuración, el manejo de notificaciones y la gestión de pagos fallidos.

### ⚙️ 1. Configuración de RabbitMQ

*   **Objetivo:** Configurar RabbitMQ para el proyecto.
*   **Detalles:**
    *   Ejecutar RabbitMQ en Docker (como se muestra en la sección general de configuración).
    *   Crear colas y exchanges necesarios para la comunicación entre microservicios.
    *   Configurar bindings para enrutar mensajes correctamente.

### 🔔 2. Sistema de Notificaciones para Nuevas Inscripciones

*   **Objetivo:** Implementar un sistema de notificaciones asíncronas para informar sobre nuevas inscripciones de miembros.
*   **Detalles:**
    *   Cuando un nuevo miembro se inscribe, un microservicio (por ejemplo, el servicio de miembros) publica un mensaje en una cola de RabbitMQ.
    *   Otro microservicio (o el mismo) consume este mensaje y realiza las acciones necesarias, como enviar un correo electrónico de bienvenida o actualizar una base de datos.
    *   **Ejemplo (en el microservicio de Miembros):**

    ```java
    @RabbitListener(queues = "inscripciones.queue")
    public void procesarInscripcion(MiembroDTO miembro) {
        System.out.println("✅ Nueva inscripción: " + miembro.getNombre());
        // Lógica adicional: enviar correo, etc.
    }
    ```

### 🔄 3. Patrón Publish/Subscribe para Cambios en Horarios de Clases

*   **Objetivo:** Implementar el patrón Publish/Subscribe para notificar a los miembros sobre cambios en los horarios de las clases.
*   **Detalles:**
    *   Cuando se modifica el horario de una clase, el microservicio de clases publica un mensaje en un exchange de tipo `fanout` o `topic`.
    *   Los microservicios o aplicaciones que estén interesados en estos cambios (por ejemplo, una aplicación móvil de los miembros) se suscriben a este exchange y reciben las notificaciones.
    *   **Ejemplo (en el microservicio de Clases):**

    ```java
    // Publicar cambio de horario
    rabbitTemplate.convertAndSend("gimnasio.exchange", "horarios.cambio", "Horario cambiado: " + clase.getNombre());

    // Consumir cambio de horario
    @RabbitListener(queues = "horarios.queue")
    public void recibirCambioHorario(String mensaje) {
        System.out.println("📅 Notificación recibida: " + mensaje);
        // Lógica adicional: actualizar la interfaz de usuario, etc.
    }
    ```

### ⚰️ 4. Configuración de Dead Letter Queue (DLQ) para Manejo de Pagos Fallidos

*   **Objetivo:** Configurar una Dead Letter Queue (DLQ) para manejar los mensajes de pago que no se pueden procesar correctamente después de varios intentos.
*   **Detalles:**
    *   Se crea una cola principal (`pagos-queue`) y una cola de letra muerta (`pagos-dlq`).
    *   La cola principal está configurada para enviar mensajes a la DLQ después de un número específico de reintentos o en caso de error.
    *   Un consumidor procesa los pagos. Si un pago falla, se lanza una excepción. Después de varios reintentos (configurados en la cola), el mensaje se mueve a la DLQ.
    *   Otro proceso (o el mismo microservicio) consume los mensajes de la DLQ para analizarlos y tomar las acciones correctivas necesarias (por ejemplo, contactar al cliente).
    *   **Ejemplo (Configuración de colas en `RabbitMQConfig`):**

    ```java
    @Configuration
    public class RabbitMQConfig {

        @Bean
        public Queue pagosQueue() {
            return QueueBuilder.durable("pagos-queue")
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "pagos-dlq")
                .withArgument("x-message-ttl", 30000) // 30 segundos
                .build();
        }

        @Bean
        public Queue pagosDLQ() {
            return QueueBuilder.durable("pagos-dlq").build();
        }
    }
    ```
    *   **Ejemplo (Procesamiento de pagos en `PagoService`):**

    ```java
    @Service
    public class PagoService {

        @RabbitListener(queues = "pagos-queue")
        public void procesarPago(PagoDTO pago) {
            try {
                System.out.println("💳 Procesando pago de: $" + pago.getMonto());
                // Lógica de procesamiento de pago
                if (Math.random() > 0.5) throw new RuntimeException("❌ Error en el pago");

                System.out.println("✅ Pago exitoso para: " + pago.getIdMiembro());
            } catch (Exception e) {
                System.out.println("❌ Error en pago. Enviando a DLQ.");
                throw new AmqpRejectAndDontRequeueException("Error en el pago, enviando a DLQ", e);
            }
        }
    }
    ```

---

## 🛠️ Tecnologías Utilizadas

-   **Spring Boot 3.3.2:** Framework para la creación de aplicaciones Java.
-   **Spring Security & Keycloak (OAuth2):** Para la autenticación y autorización de usuarios.
-   **RabbitMQ:** Sistema de mensajería asíncrona.
-   **Spring AMQP:** Abstracción de Spring para trabajar con RabbitMQ.
-   **Docker:** Plataforma para la contenerización de aplicaciones.
-   **H2 Database:** Base de datos en memoria para desarrollo y pruebas.
-   **OpenAPI & Swagger:** Herramientas para la documentación de APIs RESTful.
-   **Postman:** Herramienta para probar APIs.

---

## 🚀 Objetivos Alcanzados (Generales del Taller)

*   Configurar **RabbitMQ** y Spring AMQP
*   Implementar **notificaciones asíncronas** con RabbitMQ
*   Implementar **publish/subscribe** para cambios de horarios de clases
*   Configurar **Dead Letter Queue (DLQ)** para pagos fallidos
*   **Protección de APIs** con **OAuth2 y Keycloak**
*   Implementar **Swagger y OpenAPI** para documentar APIs
*   Probar la comunicación **asíncrona** entre microservicios

---

## 🔧 Configuración General

### 🐰 RabbitMQ

1.  **Ejecutar RabbitMQ en Docker:**

    ```bash
    docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
    ```
2.  **Acceder a la interfaz de administración:**

    *   Abrir el navegador y navegar a `http://localhost:15672`
    *   **Usuario:** `guest`
    *   **Contraseña:** `guest`
3.  **Ver las colas activas en RabbitMQ (dentro del contenedor):**

    ```bash
    docker exec -it rabbitmq bash
    rabbitmqctl list_queues name messages_ready messages_unacknowledged
    ```

### 🔑 Keycloak

1.  **Obtener Token para autenticación (ejemplo):**

    ```bash
    curl --location 'http://localhost:8080/realms/gimnasio/protocol/openid-connect/token' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'grant_type=password' \
    --data-urlencode 'client_id=miembros-service' \
    --data-urlencode 'client_secret=secret' \
    --data-urlencode 'username=train' \
    --data-urlencode 'password=train1'
    ```

### 📄 Documentación API con Swagger

*   **Microservicio de Miembros:** `http://localhost:8085/swagger-ui/index.html`
*   **Microservicio de Clases:** `http://localhost:8086/swagger-ui/index.html`

---

## 📌 Conclusión

Este taller demuestra la implementación efectiva de la comunicación asíncrona en un sistema de microservicios para un gimnasio, utilizando RabbitMQ para notificaciones, manejo de pagos fallidos y el patrón Publish/Subscribe. Además, se integra la seguridad con OAuth2 y Keycloak, y se documentan las APIs con Swagger/OpenAPI.
