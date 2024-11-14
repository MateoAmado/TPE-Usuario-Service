package tpe.Usuario.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import tpe.Usuario.dto.UsuarioLoginDTO;
import tpe.Usuario.dto.UsuarioRegistroDTO;
import tpe.Usuario.model.Usuario;
import tpe.Usuario.repository.UsuarioRepository;
import tpe.Usuario.security.JWT_Utilidades;
import tpe.Usuario.services.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "API para autenticar y registrar Usuarios")
public class AuthController {
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JWT_Utilidades jwt_utilidad;

    @Autowired
    DefaultUserService servicio_usuario;

    @Autowired
    DefaultUserService defaultUserService;



    @Operation(summary = "Validar token", description = "Valida el token JWT proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "400", description = "Encabezado de autorización inválido"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Encabezado de autorización inválido");
        }

        String token = authorizationHeader.substring(7);
        String username = jwt_utilidad.extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        boolean isValid = jwt_utilidad.validateToken(token, userDetails);

        if (isValid) {
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");
            return ResponseEntity.ok(Collections.singletonMap("role", role));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }


    @Operation(summary = "Obtener rol de usuario", description = "Obtiene el rol de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al obtener el rol")
    })
    @GetMapping("/{id}/rol")
    public ResponseEntity<String> obtenerRol(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        Usuario usuario = defaultUserService.findById(id);
        if(usuario != null) {
            return new ResponseEntity<>(usuario.getRol(), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Obtener usuario por ID", description = "Obtiene la información de un usuario por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no válido o caducado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Usuario usuario = defaultUserService.findById(id);
        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @Operation(summary = "Registro de un nuevo usuario", description = "Registra un nuevo usuario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en el registro del usuario")
    })
    @PostMapping("/registro")
    public ResponseEntity<Object> registro(@RequestBody UsuarioRegistroDTO usuario_dto) {
        Usuario usuario = this.servicio_usuario.save(usuario_dto);
        if (usuario.equals(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No fue posible guardar el usuario.");
        }

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }


    @Operation(summary = "Inicio de sesión", description = "Permite a un usuario iniciar sesión en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public String login(@RequestBody UsuarioLoginDTO usuario_dto) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario_dto.getEmail(), usuario_dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return this.jwt_utilidad.generateToken(authentication);


    }


    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Error al intentar eliminar el usuario")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> borrarUsuario(@PathVariable int id){
        Usuario u = defaultUserService.findById(id);
        if(u!=null){
            defaultUserService.delete(u);
            return new ResponseEntity<>(u, HttpStatus.NO_CONTENT);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Modificar datos de un usuario", description = "Modificar los datos de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos modificados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Error al modificar los datos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id, @RequestBody Usuario usuario){
        Usuario u = defaultUserService.findById(id);
        if(u!=null){
            Usuario actualizado = defaultUserService.update(u, usuario);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }






}
