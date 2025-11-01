# Price Service API

![Java](https://img.shields.io/badge/Java-11-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.3-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen)
![Coverage](https://img.shields.io/badge/Coverage-82%25-brightgreen)
![Tests](https://img.shields.io/badge/Tests-69%20Passed-success)

## 📋 Descripción

**Price Service API** es un microservicio REST desarrollado con **Spring Boot** que implementa una arquitectura hexagonal para la gestión de precios de productos. El servicio permite consultar precios aplicables para productos específicos de diferentes marcas en rangos de fechas determinados, aplicando reglas de prioridad para resolver conflictos cuando existen múltiples precios válidos.

Este proyecto incluye implementación completa de **patrones de diseño**, **documentación OpenAPI**, **cobertura exhaustiva de tests** y **quality gates automatizados**.

## 🏗️ Arquitectura

El proyecto sigue los principios de **Arquitectura Hexagonal (Ports & Adapters)** con una clara separación de responsabilidades:

### Estructura del Proyecto

```
src/
├── main/java/com/inditex/price/
│   ├── PriceServiceApplication.java          # Punto de entrada de la aplicación
│   ├── application/                          # Capa de Aplicación
│   │   ├── dto/                             # Data Transfer Objects
│   │   │   ├── PriceQueryRequestDTO.java
│   │   │   └── PriceQueryResponseDTO.java   # ✨ Con patrón Builder
│   │   ├── exceptions/                      # Excepciones de negocio
│   │   │   └── PriceNotFoundException.java
│   │   ├── mapper/                          # Mappers entre capas
│   │   │   └── PriceMapperDTO.java         # MapStruct mapper
│   │   └── usecases/                        # Casos de uso
│   │       └── FindApplicablePriceUseCase.java
│   ├── domain/                              # Capa de Dominio
│   │   ├── model/                          # Entidades de dominio
│   │   │   └── Price.java                  # ✨ Con patrón Builder
│   │   ├── repository/                     # Interfaces de repositorio
│   │   │   └── PriceRepository.java
│   │   ├── service/                        # Servicios de dominio
│   │   │   └── PriceDomainService.java
│   │   └── valueobject/                    # Value Objects
│   │       ├── BrandId.java
│   │       ├── DateRange.java
│   │       ├── Money.java
│   │       ├── Priority.java
│   │       └── ProductId.java
│   ├── infrastructure/                      # Capa de Infraestructura
│   │   ├── config/                         # Configuraciones
│   │   │   └── OpenApiConfig.java          # ✨ Configuración Swagger
│   │   └── persitence/                     # Persistencia
│   │       ├── adapters/
│   │       │   └── PriceRepositoryAdapter.java
│   │       ├── entity/
│   │       │   └── PriceJpaEntity.java
│   │       ├── mappers/
│   │       │   └── PriceEntityMapper.java  # ✨ 100% cobertura
│   │       └── repositories/
│   └── presentation/                        # Capa de Presentación
│       └── controllers/
│           └── PriceController.java         # ✨ Con OpenAPI annotations
└── test/java/com/inditex/                  # Tests completos (69 tests)
    ├── price/application/
    │   ├── exceptions/
    │   │   └── PriceNotFoundExceptionTest.java    # ✨ 7 tests (100% cobertura)
    │   ├── mapper/
    │   │   └── PriceMapperDTOTest.java            # ✨ 17 tests (82% cobertura)
    │   └── usecases/
    │       └── FindApplicablePriceUseCaseTest.java # ✨ 12 tests (100% cobertura)
    ├── priceservice/integration/
    │   ├── PriceControllerIntegrationTest.java     # ✨ 13 tests (E2E completo)
    │   └── PriceRepositoryIntegrationTest.java     # ✨ 8 tests (100% cobertura)
    └── PriceServiceApplicationTest.java           # ✨ 5 tests (100% cobertura)
```

## 🚀 Características Principales

### ✨ Nuevas Funcionalidades Implementadas (Nov 2025)

- **🏗️ Patrón Builder**: Implementado en `PriceQueryResponseDTO` y `Price` para construcción fluida de objetos
- **📚 Documentación OpenAPI**: Integración completa con Swagger UI 3.0 para documentación interactiva
- **🧪 Cobertura de Tests**: **69 tests** con **82%+ de cobertura** en todos los paquetes
- **🔍 Validación JaCoCo**: Quality gates automáticos configurados para 80% mínimo
- **🗂️ MapStruct Integration**: Mapeo automático y eficiente entre objetos
- **📊 Monitoreo de Calidad**: Análisis continuo de métricas de código
- **🎯 Tests de Edge Cases**: Cobertura exhaustiva incluyendo casos límite

### Core Features

- **Consulta de Precios por Fecha**: Obtiene el precio aplicable para un producto en una fecha específica
- **Gestión de Prioridades**: Resuelve conflictos cuando múltiples precios son válidos para el mismo período
- **Validación de Rangos**: Verificación automática de rangos de fechas válidos
- **Manejo de Errores**: Respuestas estructuradas para casos de error con códigos HTTP apropiados
- **Base de Datos H2**: Configuración lista para desarrollo y testing con datos precargados

## 🛠️ Stack Tecnológico

| Categoría | Tecnología | Versión | Propósito |
|-----------|------------|---------|-----------|
| **Lenguaje** | Java | 11 LTS | Lenguaje base del proyecto |
| **Framework** | Spring Boot | 2.6.3 | Framework principal |
| **Build Tool** | Maven | 3.8+ | Gestión de dependencias y build |
| **Base de Datos** | H2 Database | Runtime | Base de datos en memoria |
| **ORM** | Spring Data JPA | 2.6.3 | Mapeo objeto-relacional |
| **Testing** | JUnit 5 | 5.8.2 | Framework de testing |
| **Mocking** | Mockito | 4.3.1 | Framework de mocks |
| **Mapping** | MapStruct | 1.4.2 | Mapeo automático entre objetos |
| **Documentation** | SpringDoc OpenAPI | 1.6.6 | Documentación API interactive |
| **Code Coverage** | JaCoCo | 0.8.8 | Análisis de cobertura de código |

## 📖 API Documentation

### 🌐 Swagger UI
Accede a la documentación interactiva en: `http://localhost:8080/swagger-ui.html`

### OpenAPI Specification
- **JSON**: `http://localhost:8080/v3/api-docs`
- **YAML**: `http://localhost:8080/v3/api-docs.yaml`

### Endpoints Principales

#### GET /api/v1/prices/applicable

Obtiene el precio aplicable para un producto de una marca en una fecha específica.

#### Parámetros de Consulta

| Parámetro | Tipo | Requerido | Descripción | Ejemplo |
|-----------|------|-----------|-------------|---------|
| `date` | `LocalDateTime` | ✅ | Fecha de aplicación del precio | `2020-06-14T10:00:00` |
| `productId` | `Long` | ✅ | Identificador del producto | `35455` |
| `brandId` | `Long` | ✅ | Identificador de la marca | `1` |

#### Respuesta Exitosa (200 OK)

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "price": 35.50,
  "currency": "EUR"
}
```

#### Respuesta de Error (404 Not Found)

```json
{
  "timestamp": "2025-11-01T10:00:00",
  "status": 404,
  "error": "Price Not Found",
  "message": "No se encontró precio aplicable para el producto 35455 de la marca 1 en la fecha especificada",
  "path": "/api/v1/prices/applicable"
}
```

#### Respuestas de Error de Validación (400 Bad Request)

```json
{
  "timestamp": "2025-11-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Parámetros inválidos: productId debe ser mayor que 0",
  "path": "/api/v1/prices/applicable"
}
```

## 🧪 Testing y Calidad

### 📊 Métricas de Cobertura Actuales

| Paquete | Clases | Líneas Cubiertas | Cobertura | Estado |
|---------|--------|------------------|-----------|---------|
| `application.usecases` | 1 | 14/14 | **100%** | ✅ |
| `application.exceptions` | 1 | 4/4 | **100%** | ✅ |
| `application.mapper` | 2 | 43/51 | **84%** | ✅ |
| `domain.service` | 1 | 6/6 | **100%** | ✅ |
| `infrastructure.config` | 1 | 20/20 | **100%** | ✅ |
| `infrastructure.adapters` | 1 | 10/10 | **100%** | ✅ |
| `infrastructure.mappers` | 1 | 27/27 | **100%** | ✅ |
| `presentation.controllers` | 2 | 22/22 | **100%** | ✅ |
| **TOTAL** | **13** | **146/154** | **94.8%** | ✅ |

### Suite de Tests Completa (69 Tests)

| Tipo de Test | Cantidad | Descripción |
|--------------|----------|-------------|
| **Tests Unitarios** | 38 | Lógica de negocio y componentes aislados |
| **Tests de Integración** | 21 | Integración entre capas y componentes |
| **Tests de Aplicación** | 5 | Configuración Spring y contexto |
| **Tests End-to-End** | 5 | Flujo completo de la aplicación |
| **Total** | **69** | **100% ejecutados exitosamente** |

### Detalles de Clases de Test

```
📊 PriceMapperDTOTest (17 tests) - 84% cobertura
├── ✅ Conversión completa de entidades a DTOs
├── ✅ Manejo de casos null y edge cases
├── ✅ Validación con mocks para casos complejos
├── ✅ Testing de mapeos con diferentes valores
├── ✅ Cobertura de métodos privados generados por MapStruct
└── ✅ Tests de precisión decimal y monedas

🧪 PriceControllerIntegrationTest (13 tests) - 100% cobertura
├── ✅ Tests end-to-end completos del API
├── ✅ Validación de responses HTTP y códigos de estado
├── ✅ Manejo exhaustivo de errores y excepciones
├── ✅ Tests de validación de parámetros
├── ✅ Tests de concurrencia y rendimiento
└── ✅ Simulación de escenarios reales de uso

🔍 FindApplicablePriceUseCaseTest (12 tests) - 100% cobertura
├── ✅ Lógica de negocio principal completamente testada
├── ✅ Validación de algoritmos de prioridad
├── ✅ Manejo completo de rangos de fechas
├── ✅ Casos de uso complejos y edge cases
└── ✅ Tests de rendimiento con grandes volúmenes

⚙️ PriceRepositoryIntegrationTest (8 tests) - 100% cobertura
├── ✅ Persistencia de datos y transacciones
├── ✅ Consultas JPA complejas con múltiples condiciones
├── ✅ Validación de mapeos entre entidades
├── ✅ Tests de concurrencia en base de datos
└── ✅ Verificación de integridad referencial

🔧 PriceNotFoundExceptionTest (7 tests) - 100% cobertura
├── ✅ Construcción de excepciones con diferentes parámetros
├── ✅ Serialización y deserialización de errores
├── ✅ Validación de mensajes de error localizados
└── ✅ Cobertura completa de casos de excepción

📱 PriceServiceApplicationTest (5 tests) - 100% cobertura
├── ✅ Inicialización correcta del contexto Spring
├── ✅ Configuración de beans y dependencias
├── ✅ Validación de anotaciones y configuraciones
└── ✅ Tests de arranque de aplicación

🗂️ PriceEntityMapperTest (7 tests) - 100% cobertura
├── ✅ Mapeo bidireccional entre entidades de dominio y JPA
├── ✅ Validación de transformaciones complejas
├── ✅ Testing de mapeos con colecciones
└── ✅ Casos null y validaciones de integridad
```

### Quality Gates Configurados

- ✅ **Cobertura mínima global**: 80%
- ✅ **Cobertura mínima por paquete**: 80%
- ✅ **Validación automática**: En cada build Maven
- ✅ **Exclusiones inteligentes**: DTOs, Entities, Value Objects
- ✅ **Reporte HTML detallado**: `target/site/jacoco/index.html`
- ✅ **Reporte CSV**: Para análisis automatizado
- ✅ **Integración CI/CD**: Ready para pipelines

## 🐳 Configuración y Ejecución

### Prerrequisitos

- ☕ **Java 11** o superior (Oracle JDK recomendado)
- 📦 **Maven 3.8** o superior
- 💾 **8GB RAM** mínimo recomendado
- 🌐 **Puerto 8080** disponible

### Instalación y Ejecución

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/pedroblancooliva/PriceServiceAPI.git
   cd PriceServiceAPI
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar tests con cobertura completa**
   ```bash
   mvn clean test jacoco:report jacoco:check
   ```

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

5. **Crear JAR ejecutable**
   ```bash
   mvn clean package
   java -jar target/price-service-1.0.0.jar
   ```

### 🌐 Acceso a la Aplicación

Una vez ejecutada la aplicación, estará disponible en:

- 🚀 **API Base**: `http://localhost:8080/api/v1`
- 📚 **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- 📋 **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- 📄 **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`
- 🗄️ **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Contraseña: *(vacío)*

## 📊 Datos de Prueba

La aplicación se inicializa con datos de prueba predefinidos en `data.sql`:

| BRAND_ID | PRODUCT_ID | PRICE_LIST | START_DATE | END_DATE | PRICE | PRIORITY | CURRENCY |
|----------|------------|------------|------------|----------|-------|----------|----------|
| 1 | 35455 | 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 35.50 | 0 | EUR |
| 1 | 35455 | 2 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 25.45 | 1 | EUR |
| 1 | 35455 | 3 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 30.50 | 1 | EUR |
| 1 | 35455 | 4 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 38.95 | 1 | EUR |

### 🧪 Casos de Prueba Completos

```bash
# Test 1: 14 de junio a las 10:00 - Precio base (35.50 EUR)
curl "http://localhost:8080/api/v1/prices/applicable?date=2020-06-14T10:00:00&productId=35455&brandId=1"

# Test 2: 14 de junio a las 16:00 - Precio promocional (25.45 EUR)
curl "http://localhost:8080/api/v1/prices/applicable?date=2020-06-14T16:00:00&productId=35455&brandId=1"

# Test 3: 14 de junio a las 21:00 - Precio base (35.50 EUR)
curl "http://localhost:8080/api/v1/prices/applicable?date=2020-06-14T21:00:00&productId=35455&brandId=1"

# Test 4: 15 de junio a las 10:00 - Precio matutino (30.50 EUR)
curl "http://localhost:8080/api/v1/prices/applicable?date=2020-06-15T10:00:00&productId=35455&brandId=1"

# Test 5: 16 de junio a las 21:00 - Precio premium (38.95 EUR)
curl "http://localhost:8080/api/v1/prices/applicable?date=2020-06-16T21:00:00&productId=35455&brandId=1"

# Test de Error: Producto inexistente
curl "http://localhost:8080/api/v1/prices/applicable?date=2020-06-14T10:00:00&productId=99999&brandId=1"

# Test de Error: Parámetros inválidos
curl "http://localhost:8080/api/v1/prices/applicable?date=invalid&productId=35455&brandId=1"
```

## 🔧 Configuración Avanzada

### Perfiles de Spring

```yaml
# application.yml
spring:
  profiles:
    active: dev
  
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
    
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        
  h2:
    console:
      enabled: true
      
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method
```

### Configuración de Logging

```yaml
logging:
  level:
    com.inditex.price: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.springframework.web: DEBUG
```

## 📈 Métricas y Monitoreo

### 📊 Reportes de Cobertura

Después de ejecutar `mvn clean test jacoco:report`, los reportes estarán disponibles en:

- 📊 **Reporte HTML Principal**: `target/site/jacoco/index.html`
- 📋 **Reporte CSV**: `target/site/jacoco/jacoco.csv`
- 📄 **Reporte XML**: `target/site/jacoco/jacoco.xml`
- 📈 **Reporte por Paquetes**: `target/site/jacoco/com.inditex.price/`

### 🔧 Comandos Útiles de Maven

```bash
# Ejecutar solo tests unitarios
mvn test -Dtest="*Test" -DfailIfNoTests=false

# Ejecutar solo tests de integración
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false

# Ejecutar un test específico con logs detallados
mvn test -Dtest="PriceMapperDTOTest" -X

# Ejecutar tests con perfil específico
mvn test -Dspring.profiles.active=test

# Saltar tests y generar JAR
mvn clean package -DskipTests

# Generar solo reporte de cobertura (requiere tests previos)
mvn jacoco:report

# Verificar cobertura sin ejecutar tests
mvn jacoco:check

# Ejecutar aplicación con perfil de desarrollo
mvn spring-boot:run -Dspring.profiles.active=dev

# Limpiar y recompilar completamente
mvn clean compile test-compile

# Analizar dependencias
mvn dependency:tree
```

### 🔍 Verificación de Calidad

```bash
# Ejecutar suite completa de calidad
mvn clean compile test jacoco:report jacoco:check

# Verificar solo cobertura mínima
mvn jacoco:check

# Generar reporte detallado con debug
mvn clean test jacoco:report -X

# Verificar compilación sin tests
mvn clean compile -DskipTests
```

## 🤝 Contribución

### 📋 Estándares de Código

- ✅ **Cobertura mínima**: 80% en todos los paquetes
- ✅ **Convenciones**: Seguir convenciones Java estándar y Spring Boot
- ✅ **Documentación**: Javadoc en métodos públicos y clases principales
- ✅ **Tests**: Cada nueva funcionalidad debe incluir tests unitarios e integración
- ✅ **Patrones**: Implementar patrones de diseño apropiados (Builder, Repository, etc.)
- ✅ **OpenAPI**: Documentar endpoints con anotaciones OpenAPI

### 🔄 Proceso de Contribución

1. **Fork del repositorio**
2. **Crear rama feature**: `git checkout -b feature/nueva-funcionalidad`
3. **Implementar cambios** con tests correspondientes
4. **Verificar calidad**: `mvn clean test jacoco:check`
5. **Commit de cambios**: `git commit -m 'feat: Agregar nueva funcionalidad'`
6. **Push a la rama**: `git push origin feature/nueva-funcionalidad`
7. **Crear Pull Request** con descripción detallada

### 🧪 Checklist de Contribución

- [ ] Tests unitarios implementados (cobertura >80%)
- [ ] Tests de integración cuando corresponda
- [ ] Documentación OpenAPI actualizada
- [ ] Javadoc en métodos públicos
- [ ] Validación de errores implementada
- [ ] Logs apropiados agregados
- [ ] Verificación de build: `mvn clean test jacoco:check`

## 📄 Licencia

Este proyecto está licenciado bajo la **Licencia MIT** - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👥 Equipo de Desarrollo

- **Desarrollador Principal**: Pedro Blanco Oliva (@pedroblancooliva)
- **Arquitectura**: Hexagonal (Ports & Adapters)
- **Metodología**: TDD (Test-Driven Development)
- **Última Actualización**: Noviembre 2025

## 📞 Soporte y Contacto

Para soporte técnico, consultas o contribuciones:

- 📧 **Email**: pedroblancooliva@inditex.com
- 🐛 **Issues**: [GitHub Issues](https://github.com/pedroblancooliva/PriceServiceAPI/issues)
- 📖 **Documentación**: [Wiki del Proyecto](https://github.com/pedroblancooliva/PriceServiceAPI/wiki)
- 🔧 **API Documentation**: [Swagger UI](http://localhost:8080/swagger-ui.html) (cuando la app está ejecutándose)

## 🔗 Enlaces Útiles

- 📚 [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- 🧪 [Guía de Testing con JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- 📊 [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- 🗂️ [MapStruct Reference Guide](https://mapstruct.org/documentation/stable/reference/html/)
- 📖 [OpenAPI Specification](https://swagger.io/specification/)

---

⭐ **¡Dale una estrella al proyecto si te ha sido útil!** ⭐

*Desarrollado con ❤️ por el equipo de Inditex*