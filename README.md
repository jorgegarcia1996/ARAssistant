# ARAssistant

El proyecto consiste en una aplicación Android mediantye la cual podrémos administrar las gafas de Realidad Aumentada.
En esta aplicación podemos incluir capas con información o elementos 3D los cuales serán superpuestos en el entorno del usuario.

La app Android será utilizada como servidor de contenidos para las gafas Epson Moverio BT-350 y con servicios para recoger o proveer datos a la aplicación instalada en las gafas de Realidad Aumentada. Gracias al posicionamiento de las capas, el usuario puede moverse libremente y los elementos aparecerás por proximidad, indicando tareas, descripciones, recordatorios, notificaciones, etc...

[AR App vuforia](https://drive.google.com/file/d/1uSet_6U3CkLxNrmYnMyoFpa0xbtka-0p/view?usp=sharing)

## Avances 24/03/2020

***Jorge:***
* Diagrama UML de casos de uso para la app móvil.
* Diagrama E/R de la base de datos.
* Árbol JSON con la estructura de la BBDD para Firebase.

## Avances 29/03/2020

***Jorge:***
* Tablas de requisitos de la base de datos.

## Avances 02/04/2020

***Jorge y Rafaél:***
* Mockup de la App Movil.
* Diseño de pantallas, [Demo online](https://www.figma.com/proto/pXeGToLrAQjJCZh8pPeCtl/Arassistant_Pantallas?node-id=1%3A5&scaling=min-zoom)

## Avances 17/04/2020

***Jorge:***
* Layout del login creado.
* Logo e icono para la app-móvil creados.
* Mockup y diseño de pantallas actualizado.

***Lino:***
* Layout del registro.
* Diseño del tutorial de primeros pasos en la app.
* Conexión firebase.

## Avances 18/04/2020

***Jorge:***
* Base de datos creada.
* Login completado y funcional.

## Avances 22/04/2020

***Lino:***
* Registro completado y funcional.

***Jorge:***
* Navegación desde el login al registro.
* Cambio de contraseña desde la pantalla de login.
* Optimización de código.

## Avances 24/04/2020

***Lino***
* Guardar los datos del usuario en Firebase Database.

## Avances 25/04/2020

***Jorge***
* Modelo para las tareas creado.
* Fragmentos para el home y las tareas creados.
* Navegación entre fragmentos implementada.

## Avances 29/04/2020

***Jorge***
* Listado de tareas sin filtro.
* Crear nueva tarea y guardarla en FB.
* Generador IDs.

***Lino***
* Pantalla de perfil creada.

## Avances 30/04/2020

***Jorge***
* CRUD de las tareas implementado.

## Avances 09/05/2020

***Lino***
* Menú lateral creado.
* Generador de QR.
* Navigation drawer implementado.

***Jorge***
* Añadidas las categorías a las tareas.
* Filtro por categorías de la lista de tareas.
* Opción de recordar usuario y contraseña en el login.

## Avances 19/05/2020

***Jorge***
* Base de datos local para las categorías implementada.
* Icono y color para cada categoría.

## Avances 26/05/2020

***Jorge***
* Ahora se puede subir una imagen para cada tarea.
* Tamaño de imágenes limitado a 4096x4096px o 1MB.
* Se puede cambiar la images de una tarea al editar la dicha tarea.

## Avances 28/05/2020

***Jorge***
* Convertidas las clases del paquete '*Utils*' a Singleton
* Fragmentos y actividades para los recordatorios creadas

## Avances 29/05/2020

***Jorge***
* Creación de recordatorios implementada.

## Avances 30/05/2020

***Jorge***
* CRUD de los recordatorios implementado.
* Menú contextual para los recordatorios.
* Solo se muestran los recordatorios del día seleccionado.
* No se pueden crear recordatorios en el pasado.
* Cada usuario solo puede acceder a sus recordatorios.

## Avances 01/06/2020

***Jorge***
* Al pulsar en el botón de recuperar contraseña, ahora se abre un dialog pidiendo el email.
* En los recordatorios, si hay algún día con un recordatorio, se marca en el calendario.

## Avances 04/06/2020

***Jorge***
* Borrar cuenta implementado
* Código comentado

## Avances 06/06/2020

***Jorge***
* Botones para ir a tareas o recordatorios en la pantalla principal

## Avances 11/06/2020

***Rafael y Jorge***
* App web para la administración de usuario desplegada en el siguiente [enlace](https://arassistant-2e660.web.app/home)


## Avances 18/09/2020

***Jorge***
* Proyecto android actualizado.

## Avances 26/09/2020

***Jorge***
* Al crear o editar una tarea, la parte de subir una imagen ahora abre la cámara para tomar una foto directamente sin abrir la galería.

## Avances 29/09/2020

***Jorge***
* Creados los botones para abrir la galería, tomar una foto o grabar un vídeo al crear o editar una tarea.
* El boton de Abrir la galería permite seleccionar una imagen de la galería del móvil para subirla.
* Si la tarea ya tiene una imagen, esta se sustituye por la imagen seleccionada de la galería o la imagen tomada con la cámara al guardar la tarea.