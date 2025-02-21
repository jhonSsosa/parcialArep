# Calculadora HTTP con Fachada y Backend

El repositorio incluye un servidor de fachada HTTP que se comunica con un servidor de calculadora en otro puerto para realizar operaciones matemáticas utilizando la reflexión en Java. Los servicios están disponibles a través de HTTP y responden a solicitudes `GET`.

Requerimientos

- Java 8 o superior.
- Un puerto disponible para ejecutar el servidor de fachada (por defecto `35000`).
- Un puerto disponible para ejecutar el servidor de calculadora (por defecto `36000`).

Descripción 
1. Servicio de la Fachada

- URL de la fachada: `http://localhost:35000`
  
  El servidor de fachada proporciona una interfaz web donde los usuarios pueden ingresar operaciones matemáticas y números para calcular el resultado.

- Ruta principal `/`**:  
  La fachada muestra un formulario HTML donde el usuario puede ingresar una operación (como `sqrt`, `pow`, `max`, etc.) y una lista de números. Cuando el usuario envía el formulario, se hace una solicitud `GET` al servidor de la calculadora para obtener el resultado.

- Ruta `/computar?comando={operacion}&numeros={numeros}`**:
  La fachada envía la solicitud al servidor de la calculadora para calcular el resultado de una operación, pasando los parámetros `comando` (nombre de la operación) y `numeros` (lista de números separados por coma).

2. Servicio de la Calculadora

- URL de la calculadora: `http://localhost:36000`

  El servidor de la calculadora maneja las solicitudes que le envía la fachada. Realiza cálculos utilizando las operaciones de la clase `Math` en Java.

- Ruta `/compreflex?comando={operacion}&numeros={numeros}`:
  Este servicio acepta una operación y una lista de números, luego usa reflexión para invocar la operación correspondiente de la clase `Math` de Java. Las operaciones soportadas son las que aceptan uno o dos parámetros de tipo `double` (por ejemplo: `sqrt`, `pow`, `max`, etc.).

Requisitos previos

Antes de ejecutar los servidores, asegúrate de tener Java instalado en tu sistema. Puedes verificar si Java está instalado ejecutando el siguiente comando:

```bash
java -version

Instalacion

Para ejecutar la instalacion se debe de clonar el repositorio con el link https://github.com/jhonSsosa/parcialArep

- Luego ingresar a la carpeta del directorio en donde tenga ubicado el repositorio con el comando: cd parcialArep

Ejecucion

- El servidor de la calculadora escucha en el puerto 36000. Para iniciar el servidor, ejecuta el siguiente comando:

java -cp target/classes org.example.CalculadoraHttpServer

Con este en ejecucion escribimos en el navegador el siguiente link para procesar la solicitud:

http://localhost:36000/compreflex?comando=(operacion)&numeros=(numeros)

En los cuales escribiremos en la operacion el tipo de operacion que en la libreria Math se quiere ejecutar, y en numeros el o los numeros separados en "," que se quieren operar para la operacion.

- Para el servidor de la fachada que escucha en el puerto 350000, se debe ejecutar el siguiente comando:

java -cp target/classes org.example.CalculadoraHttpServer

Con este en ejecucion escribimos en el navegador el siguiente link para procesar la solicitud:

http://localhost:35000/

Esto nos redirigira a un html en el cual se podra ingresar tanto el comando de la libreria math como el o los numeros separados en ",", para asi poder generar la respuesta a la misma solicitud.



