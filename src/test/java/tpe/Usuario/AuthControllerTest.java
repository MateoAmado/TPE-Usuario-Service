package tpe.Usuario;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import tpe.Usuario.controller.AuthController;
import tpe.Usuario.dto.UsuarioRegistroDTO;
import tpe.Usuario.model.Usuario;
import tpe.Usuario.services.DefaultUserService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefaultUserService servicio_usuario;

    @MockBean
    private AuthenticationManager authManager;

    @BeforeEach
    public void setUp() {}

    @Test
    public void testRegistro() throws Exception {
        UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO("password", "test@example.com", "USER", "Nombre", "Apellido", 123456789);
        when(servicio_usuario.save(any())).thenReturn(new Usuario());
        mockMvc.perform(post("/auth/registro")
                        .contentType("application/json")
                        .content("{\"password\":\"password\",\"email\":\"test@example.com\",\"rol\":\"USER\",\"nombre\":\"Nombre\",\"apellido\":\"Apellido\",\"nro_celular\":123456789}"))
                .andExpect(status().isOk());
    }
}
