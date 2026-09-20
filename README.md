# App-DB-Api

Aplicacion movil para Android desarrollada en Java que consume la API publica de Dragon Ball para buscar y visualizar informacion de personajes en tiempo real.

## Caracteristicas

- Busqueda de personajes por identificador (ID).
- Consumo de servicios REST con peticiones asincronas mediante la libreria Volley.
- Descarga y visualizacion dinamica de imagenes de personajes utilizando ImageRequest.
- Parseo y visualizacion de datos: nombre, nivel de ki, raza y genero.
- Visualizacion de transformaciones del personaje mediante cuadro de dialogo (AlertDialog).
- Validacion de entrada y control de errores HTTP.
- Limpieza y reinicio automatico de campos en cada consulta.

## Tecnologias utilizadas

- Lenguaje: Java
- Plataforma: Android SDK
- Libreria de red: Google Volley (JsonObjectRequest, ImageRequest)
- Sistema de compilacion: Gradle
- API externa: [Dragon Ball API](https://web.dragonball-api.com/)

## Estructura del proyecto

- `app/src/main/java/com/example/applistas/BuscadorPersonaje.java`: Actividad principal con la logica de peticiones de red, parseo JSON y gestion de la interfaz.
- `app/src/main/res/layout/activity_buscador_personaje.xml`: Diseno de la interfaz de usuario con controles de entrada, imagen y campos informativos.
- `app/src/main/AndroidManifest.xml`: Declaracion de componentes y permisos de red (`INTERNET`).

## Requisitos de ejecucion

- Android Studio
- JDK 17 o superior
- Dispositivo Android fisico o emulador (o Waydroid) con version minima compatible configurada en el proyecto.

## Instalacion y ejecucion

1. Clonar el repositorio:
   ```bash
   git clone git@github.com:CarlosEvCode/App-DB-Api.git
   ```
2. Abrir el proyecto en Android Studio.
3. Sincronizar las dependencias con Gradle.
4. Compilar y ejecutar en un dispositivo o emulador.
