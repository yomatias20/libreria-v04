# PROYECTO LIBRERÍA

¡Mi primer trabajo!!!

Este es un ejercicio de desarrollo web con fines puramente académicos. 
Se trata de un sistema básico de librería (en realidad, biblioteca) que se provee de una base de datos de libros, autores, editoriales, clientes, préstamos y fotos que interactúan entre sí.



Está desarrollado en **Java 17** con **Spring Boot 3.1.4**. Utiliza además **Spring Security** y el motor de plantillas **Thymeleaf**.
Las vistas están en **HTML** y **CSS**. Si bien originalmente utilizaba también **Bootstrap**, fui eliminando las clases de este framework a medida que iba aprendiendo y descubriendo cómo crearlas yo mismo en CSS (básicamente, me refiero a los layouts `display:flex` y `display:grid`).
El motor de base de datos es **MySQL**.

El proyecto incorpora la funcionalidad de carga y visualización de imágenes.

La zona horaria del driver está configurada para Europa / Madrid.

Las vistas no están adaptadas a distintos tamaños de pantalla, sino a una pantalla de ordenador de aproximadamente 1600 pixels de ancho.
En pantallas de tamaño superior se verá en condiciones, aunque puede ser que algunos elementos no queden convenientemente centrados.

**Puedes ejecutarlo** ya que he incluido una pequeña base de datos (ficticia). 

**Instrucciones para ejecutar:**

1) En MySQL, ejecutar la siguiente query para crear la base de datos:

```
CREATE DATABASE libreria_v04;
```

2) En caso de ser necesario, modificar las primeras líneas del archivo `src/main/resources/application.yml`, donde se configura el acceso a la base de datos con los siguientes parámetros:

```
Puerto: 3306
Usuario: root 
Password: root
```

3) Ejecutar el proyecto. 

Al ejecutarse, el archivo `src/main/resources/schema.sql` creará la estructura de las tablas, y el archivo `src/main/resources/data.sql` introducirá los datos necesarios para comenzar a operar.

4) Abrir el navegador web con la dirección `localhost:8080`. 

En la página principal, se puede crear un nuevo usuario o acceder con uno ya existente.

Los usuarios existentes habilitados para acceder al sistema son todos aquellos incluidos en la tabla `cliente` de la base de datos, y acceden mediante **email** y **password**.

Los clientes, a la vez que usuarios, se dividen en dos categorías: **administrador** (dispone de permisos de administrador y usuario) y **usuario** (permiso solo de usuario).

Los datos para acceder **como administrador** son los siguientes:
**Email**: admin@libreria.com
**Password**: admin1

A su vez, como administrador se puede acceder al email de todos los demás usuarios, no así a sus passwords (encriptados).
Para poder acceder como usuario con cualquiera de ellos, la contraseña es la misma en todos (excepto el administrador).
**Password del resto de usuarios**: 123456

El administrador también puede acceder en modo usuario, y a la vez puede designar otros administradores (previamente dados de alta como usuarios).
