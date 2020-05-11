# ARAssistant

El proyecto consiste en una aplicación Android mediantye la cual podrémos administrar las gafas de Realidad Aumentada.
En esta aplicación podemos incluir capas con información o elementos 3D los cuales serán superpuestos en el entorno del usuario.

La app Android será utilizada como servidor de contenidos para las gafas Epson Moverio BT-350 y con servicios para recoger o proveer datos a la aplicación instalada en las gafas de Realidad Aumentada. Gracias al posicionamiento de las capas, el usuario puede moverse libremente y los elementos aparecerás por proximidad, indicando tareas, descripciones, recordatorios, notificaciones, etc...

## Checkpoint 11/05/2020

### Login

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Login.png" alt="Login"
     width="25%"/>

Al iniciar la app, la primera actividad que se abre es la de login, desdeella podemos acceder al registro pulsando en el botón *Regístrate ahora*. 

Para iniciar sesión será obligatorio introducir el email y contraseña, bajo estos campos hay un checkbox para habilitar si se guardan los datos del login en la app o no. Bajo el check hay un botón para recuperar la contraseña, para ello debemos poner el correo en el campo email del login, esto lo cambiaré para que al pulsar en el botón aparezca un dialog pidiendo el email para la recuperación de la contraseña.

Bajo el botón de recuperar la contraseña se encuentra el botón de login.

<hr/>

### Registro

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Registro-1.png" alt="Registro"
     width="25%"/>

Para registrarse es necesario introducir el nombre, apellido/s, email y contraseña, esta última hay que repetirla.
Para completar el registro hay que pulsar en el botón "✓" de arriba a la derecha, para cancelarlo se debe pulsar en "✕", arriba a la izquierda.

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Registro-2.png" alt="Validación registro"
     width="25%"/>
     
Los campos tienen una validación mínima, siendo que ningún campo debe estar vacío, el email debe ser un email, la contraseña debe ser al menos de 6 carácteres de longitud y deben coincidir al repetir la contraseña.

<hr/>
     
### Home

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Main.png" alt="Home"
     width="25%"/>

Al iniciar sesión se abre la pantalla de Home, de momento solo está puesto el logo de la app, perro en esta pantalla habrá una descripción de la app y, si da tiempo, un pequeño tutorial al iniciar sesión por primera vez.

<hr/>

### Menú lateral

Para acceder al menú lateral  hay que pulsar en "☰" al lado del nombre de la app.

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Menu-lateral.png" alt="Menú"
     width="25%"/>

Al menú se puede acceder desde cualquier actividad (excepto Login y Registro).

En la cabecera del menú encontramos la imagenb de perfil del usuario, su nombre y correo. La siguiente sección corresponde a las pantallas de *Home*, *Perfil*, *Tareas*, *Recordatorios*. Luego encontramos los botonas de *Ajustes* y *Cerrar sesión* (aún faltan por implementar los Recordatorios y los ajustes).
     
<hr/>


### Perfil

En el perfil encontramos la información del usuario y opciones de la cuenta. 

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Perfil.png" alt="Perfil"
     width="25%"/>

La opción *Vincular gafas* es para vincular las gafas de RA a la cuenta de la app movil, para ello se genera un código QR el cual se escaneará con las gafas para realizar la vinculación.

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Codigo-QR.png" alt="Código QR"
     width="25%"/>

Los botones *Configurar familia* y *Notificaciones* no están implementados aún.

En las opciones de  cuenta podemos *Cerrar sesión* o *Borrar la cuenta*, esta última aún no está implementada.
     
<hr/>

### Tareas

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Lista-tareas.png" alt="Lista de tareas"
     width="25%"/>

Al entrar en *Tareas* desde el menú lateral, lo primero que encontramos es la lista de tareas, con un filtro por categorías y un botón para crear una nueva tarea.

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Crear-tarea.png" alt="Nueva tarea"
     width="25%"/>
     
En la pantalla de crear una tarea encontramos 2 botones, *Volver* para volver a la lista de tareas y *Guardar* que guarda la tarea en la base de datos. 

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Crear-tarea-titulo.png" alt="Título obligatorio"
     width="25%"/>

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Crear-tarea-categorias.png" alt="Categorías"
     width="25%"/>

Para guardar una tarea el título es obligatorio y la categoría por defecto es la de *Salud* y podemos elegir entre 5: *Salud*, *Familia*, *Comida*, *Rutina*, *Otros*.

En la lista de tareas, para ver los detalles de una tarea, solo hay que pulsar en ella y se muestra un dialog con la información de la tarea y varias opciones:

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Detalles-tarea.png" alt="Detalles de la tarea"
     width="25%"/>

* **Borrar**: Borra la tarea de la base de datos y de la lista. Actualmente hay un bug que hace que si hay más tareas, estas se repitan varias veces en la lista al burrar una tarea, se arregla saliendo y entrando de nuevo en la lista de tareas.

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Eliminar-tarea.png" alt="Eliminar tarea"
     width="25%"/>

* **Editar**: Permite editar la tarea, para ello se mandan los datos de la tarea a la pantalla de *Crear tarea* y el botón de *Guardar* en este caso modifica la tarea correspondiente en la base de datos en lugar de crear una nueva.

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Editar-tarea.png" alt="Editar tarea"
     width="25%"/>

* **Cerrar**: Cierra el dialog.

El filtro por categorías por defecto muestra todas las tareas, pero según la categoría que selecciones, te muestra solo las tareas de esa categoría.

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Filtro-lista-tareas.png" alt="Filtro 1"
     width="25%"/>

<img src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-11-mayo/Capturas/Filtro-lista-tareas-2.png" alt="Filtro 2"
width="25%"/>

<hr/>

## To-Do

* **Recordatorios con calendario**
* **Ajustes**
* **Configurar familia**
* **Actualizar mockup**
* **Borrar cuenta**
* **Cambiar las categorías a una base de datos local**
* **Implementar el poder subir imágenes, vídeos o audio y vincularlo a las tareas**
* **Dialog para la recuperación de la contraseña**
* **Añadir descripción de la app en la pantalla de home (si da tiempo añadir un pequeño tutorial al iniciar sesión por primera vez)**
* **Editar perfil e imagen de perfil**
* **Notificaciones**
