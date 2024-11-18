# Usuario Service
### Este el servicio que maneja la entidad de Usuario.

## Endpoints:

### Base url: http://localhost:8090/auth
- #### auth/registro(POST): 
     #### Registra a un usuario en el sistema.  
  {  
    &nbsp; "password": "TUDAI",  
      &nbsp; "email": "mateoAmado@gmail.com",  
    &nbsp; "apellido": Amado,  
     &nbsp; "nombre": Mateo,  
     &nbsp; "nro_celular": 0121212,  
     &nbsp; "rol": "ADMIN",  
     &nbsp; "longitud": 200,  
     &nbsp; "latitud": 6700 
 
  }
- #### auth/login(POST):
  #### Busca al usuario por email y password en el sistema y devuelve un token.  
  {  
      &nbsp; "password": "TUDAI",  
      &nbsp; "email": "mateoAmado@gmail.com",  
  }

- #### auth/validateToken(POST):  
   Verifica la disponibilad del token.

- #### auth/{idUsuario}(GET):
  Devuelve el usuario que contiene la idUsuario solicitada.  
- #### auth/{idUsuario}/rol(GET):
  Devuelve el rol del usuario del IdUsuario solicitado.
- #### auth/{idUsuario}/ubicacion(GET):
  Devuelve la latitud y longitud del IdUsuario solicitado.
- #### auth/{idUsuario}(PUT):
  Actualiza el usuario que contenga la id IdUsuario con los parametros solicitados.  
   {  
        &nbsp; "password": "TUDAI2",  
      &nbsp; "email": "mateoAmado@gmail.com",  
    &nbsp; "apellido": AmadoS,  
     &nbsp; "nombre": MateoS,  
     &nbsp; "nro_celular": 01212212,  
     &nbsp; "longitud": 200,  
     &nbsp; "latitud": 6700 
   }  
Solo los parametros dentro del JSON pueden ser actualizados

- #### auth/{idUsuario}(DELETE):
  Borra el usuario (si existe) con la idUsuario colocada.
