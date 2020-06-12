# ARAssistant

El proyecto consiste en una aplicación Android mediantye la cual podrémos administrar las gafas de Realidad Aumentada.
En esta aplicación podemos incluir capas con información o elementos 3D los cuales serán superpuestos en el entorno del usuario.

La app Android será utilizada como servidor de contenidos para las gafas Epson Moverio BT-350 y con servicios para recoger o proveer datos a la aplicación instalada en las gafas de Realidad Aumentada. Gracias al posicionamiento de las capas, el usuario puede moverse libremente y los elementos aparecerás por proximidad, indicando tareas, descripciones, recordatorios, notificaciones, etc...

## Entrega 12/06/2020

El proyecto consta de 3 aplicaciones diferentes, la app movil creada en android nativo, la app web creada utilizando ReactJS y la app de realidad aumentada que ha sido creada utilizando Unity3D.

El proyecto tiene 3 desarrolladores principales, cada uno encargado de una o varias de las apps del proyecto, siendo estos:
* **Lino Haller Rios**: Encargado del registro, perfil de usuario y menú principal de la app móvil y la app de realidad aumentada.
* **Rafaél Aragón Rodríguez**: Encargado de crear y comunicar la app web con la base de datos de firebase.
* **Jorge García Molina**: Encargado de desplegar la app web en firebase y la app móvil excepto registro, perfil de usuario y menú principal.

### Enlaces

**Vídeos**
* Video de las [app movil y web](https://www.youtube.com/watch?v=NyjX2azUI2c)
* Video sobre la app de [Realida Aumentada](https://www.youtube.com/watch?v=JZ7RfcuMaPY)

**Demos online**
* Demo online de la [app móvil](https://www.figma.com/proto/pXeGToLrAQjJCZh8pPeCtl/Arassistant_Pantallas_App-Movil?node-id=67%3A95&scaling=scale-down)
* Demo online de la [app web](https://www.figma.com/proto/NoyU4wde7UXZ3LIGUhWpUM/ARAssistant-App-Web?node-id=1%3A2&scaling=min-zoom)

**Aplicaciones**
* [Enlace a la apk AR](https://drive.google.com/file/d/1Zybkof0uw4amvnuQkw3QAQhCJKvJgWxf/view?usp=sharing)

## Aplicacion Web
La [app web](https://arassistant-2e660.web.app/home) se encuentra desplegada en firebase hosting.

La aplicación web será utilizada para la gestión de usuarios del proyecto ARAssistant.

<p align="center">
  <img width="70%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/AppWeb-Home.PNG">
</p>

Al entrar en la app web, se muestra una descripción del proyecto, en el footer se encuentran los nombres de los participantes en el proyecto, un enlace al repositorio ne GitHub y unas imágenes de Dualiza Bankia, FP Empresa y FP IES Campanillas.

Al hacer click sobre el botón de 'Admin', se abre la pantalla de 'Login' si no hemos iniciado sesión en la web, o directamente se muestra la pantalla de administración de usuarios de la app si ya tenemos la sesión iniciada.

<p align="center">
  <img width="70%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/AppWeb-Login.png">
</p>

Para loguearnos debemos introducir un correo electrónico y contraseña de una cuenta creada en la app movil.

<p align="center">
  <img width="70%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/AppWeb-Admin.png">
</p>

En la página de administración se muestra una tabla con los datos de los usuarios de la app movil, al clicar en el botón de editar de puede cambiar el nombre y los apellidos del usuario seleccionado. Para cerrar sesión solo debemos pulsar en el botón de 'Logout' y nos llevará a la pantalla de 'Home'.

<hr/>

## App Movil

### Login

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/01-Login.png">
</p>

Al iniciar la app, la primera actividad que se abre es la de login, desde ella podemos acceder al registro pulsando en el botón *Regístrate ahora*. 

Para iniciar sesión será obligatorio introducir el email y contraseña, bajo estos campos hay un checkbox para habilitar si se guardan los datos del login en la app o no. 

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/03-Recuperar_pswd.png">
</p>

Bajo el check hay un botón para recuperar la contraseña, al pulsarlo se muetra un dialog pidiendo el email de la cuenta de recuperación de contraseña. Al introducir el email automáticamente se manda un correo para restablecer la contraseña del usuario.

Bajo el botón de recuperar la contraseña se encuentra el botón de login.

<hr/>

### Registro

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/02-Registro.png">
</p>

Para registrarse es necesario introducir el nombre, apellido/s, email y contraseña, esta última hay que repetirla.
Para completar el registro hay que pulsar en el botón "✓" de arriba a la derecha, para cancelarlo se debe pulsar en "✕", arriba a la izquierda.

Los campos tienen una validación mínima, siendo que ningún campo debe estar vacío, el email debe ser un email, la contraseña debe ser al menos de 6 carácteres de longitud y deben coincidir al repetir la contraseña.

<hr/>

### Home

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/04-Home.png">
</p>

Al iniciar sesión se abre la pantalla de Home, en ella se muestra el logotipo de la app y 2 botones, el de 'Tareas' abre la pantalla de tareas y el de 'Recordatorios' abre los recordatorios.

<hr/>

### Menú lateral

Para acceder al menú lateral  hay que pulsar en "☰" al lado del nombre de la app.

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/05-Menu_lateral.png">
</p>

Al menú se puede acceder desde cualquier actividad (excepto Login, Registro y Perfil).

En la cabecera del menú encontramos la imagen de perfil del usuario, su nombre y correo. La siguiente sección corresponde a las pantallas de *Home*, *Perfil*, *Tareas*, *Recordatorios*. Luego encontramos los botonas de *Ajustes* y *Cerrar sesión* (aún faltan por implementar los ajustes).

<hr/>

### Perfil

En el perfil encontramos la información del usuario y opciones de la cuenta. 

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/06-Perfil.png">
</p>

La opción *Vincular gafas* es para vincular las gafas de RA a la cuenta de la app movil, para ello se genera un código QR el cual se escaneará con las gafas para realizar la vinculación.

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/07-Codigo_QR.png">
</p>

Los botones *Configurar familia* y *Notificaciones* no están implementados aún.

En las opciones de cuenta podemos *Cerrar sesión* o *Borrar la cuenta*, la primera cierra la sesión y te lleva a la pantalla de login y la segunda cierra la sesión, borra la cuenta y te lleva a la pantalla de login.

<hr/>

### Tareas

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/08-Lista-tareas.png">
</p>

Al entrar en *Tareas* desde el menú lateral, lo primero que encontramos es la lista de tareas, con un filtro por categorías y un botón para crear una nueva tarea.

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/10-Crear tarea.png">
</p>

En la pantalla de crear una tarea encontramos 2 botones, *Volver* para volver a la lista de tareas y *Guardar* que guarda la tarea en la base de datos.

Para guardar una tarea el título es obligatorio y la categoría por defecto es la de *Familia* y podemos elegir entre 5: *Salud*, *Familia*, *Comida*, *Rutina*, *Otros*. Las categorías están almacenadas en una base de datos local.

En la lista de tareas, para ver los detalles de una tarea, solo hay que pulsar en ella y se muestra un dialog con la información de la tarea y varias opciones:

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/09-Detalles-tarea.png">
</p>

* **Borrar**: Borra la tarea de la base de datos y de la lista. Actualmente hay un bug que hace que si hay más tareas, estas se repitan varias veces en la lista al burrar una tarea, se arregla saliendo y entrando de nuevo en la lista de tareas.

* **Editar**: Permite editar la tarea, para ello se mandan los datos de la tarea a la pantalla de *Crear tarea* y el botón de *Guardar* en este caso modifica la tarea correspondiente en la base de datos en lugar de crear una nueva.

* **Cerrar**: Cierra el dialog.

El filtro por categorías por defecto muestra todas las tareas, pero según la categoría que selecciones, te muestra solo las tareas de esa categoría.

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/11-Categorías.png">
</p>

<hr/>

### Recordatorios

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/12-Calendario.png">
</p>

Al acceder a los recordatorios, se muestra un botón para crear un recordatorios en el día seleccionado (por defecto se selecciona el día de hoy) y un calendario en el que aparecen marcados los días en los que hay algún recordatorio.

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/16-Crear_editar_recordatorio.png">
</p>

Al crear un recordatorio debemos tener cuidado de no seleccionar un día pasado, porque no se pueden crear recordatorios en el pasado.
Los recordatorios tienen 3 campos, el título que es obligatorio, la descripción que es opcional y la hora, que por defecto selecciona la hora actual.

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/13-Lista_recordatorios.png">
</p>

Si el día seleccionado en el calendario tiene algún recordatorio, se muestran debajo del calendario. Si mantenemos presionado un segundo algún recordatorio se muestra un menú contextual con diferentes opciones: 

* **Editar**: Abre la pantalla de crear recordatorio y le manda los datos del mismo a los campos para poder modificarlos. Si el recordatorio es del día actual y se cambia la hora a una hora que ya ha pasado, no se puede guardar el recordatorio.

* **Ver recordatorio**: Muestra la información del recordatorio en un dialog.

<p align="center">
  <img width="25%"src="https://github.com/jorgegarcia1996/ARAssistant/blob/Entrega-12-Junio/Dise%C3%B1o/Capturas/15-Detalles_recordatorio.png">
</p>

* **Borrar**: Borra el recordatorio de la base de datos y de la lista.

## App Realidad Aumentada

La aplicación de realidad aumentada es la que se usará con las gafas epson moverio, la app está hecha con unity y vuforia para la realidad aumentada. La App está pensada para funcionar junto a la otra y complementarse. La app de vuforia nos permite vincular unas gafas con nuestra cuenta de firebase en de la app android, una vez hecho esto, podremos usar la aplicación como ayuda para ver las tareas y recordatorios que tenemos puestos con nuestra cuenta. Podremos ver las capas de información con las gafas puesta y así ayudarnos a recodar cosas.

La aplicación todavia está en desarrollo y en un futuro contará con muchas más funciones y un diseño mejorado.

[Enlace al video de la app](https://drive.google.com/file/d/1DyTJAXEdP24XP4GLC0uYEyydadxA4Yz_/view?usp=sharing)

[Enlace a la apk AR](https://drive.google.com/file/d/1Zybkof0uw4amvnuQkw3QAQhCJKvJgWxf/view?usp=sharing)
