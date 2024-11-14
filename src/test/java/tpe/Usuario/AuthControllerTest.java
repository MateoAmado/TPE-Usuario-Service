package tpe.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import tpe.Usuario.dto.UsuarioLoginDTO;
import tpe.Usuario.dto.UsuarioRegistroDTO;
import tpe.Usuario.model.Usuario;
import tpe.Usuario.services.DefaultUserService;
import tpe.Usuario.services.DefaultUserServiceImpl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc 
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DefaultUserServiceImpl defaultUserService;
    private String authToken;
    private static int id;


    @BeforeAll
    public static void setUp(@Autowired DefaultUserServiceImpl defaultUserService){
        UsuarioRegistroDTO usuario = new UsuarioRegistroDTO("password", "example@gmail.com",
                "ADMIN", "Mateo", "Amado", 1111, 10L, 20L);
        Usuario u = defaultUserService.save(usuario);
        id = u.getId();
    }

    @BeforeEach
    public void obtenerToken() throws Exception {
        UsuarioLoginDTO loginUsuario = new UsuarioLoginDTO("example@gmail.com", "password");
        ObjectMapper objectMapper = new ObjectMapper();
        String loginJson = objectMapper.writeValueAsString(loginUsuario);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(loginJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        authToken = result.getResponse().getContentAsString();
    }

    @Test
    public void registrarUsuarioTest() throws Exception{
        UsuarioRegistroDTO usuariodto = new UsuarioRegistroDTO("passwords",
                "ejemplo2@gmail.com", "USER", "Mateos", "Amados", 11121, 12L ,10L);

        ObjectMapper objectMapper = new ObjectMapper();
        String usuarioJson = objectMapper.writeValueAsString(usuariodto);

        System.out.println(usuarioJson);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/registro")
                        .content(usuarioJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void getRol() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/{id}/rol", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetUsuarioId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void testValidarToken() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/validateToken")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//Solucionar El actualizar usuario, q no actualice el mismo
 /*
    @Test
    public void putUsuario() throws Exception{

        UsuarioRegistroDTO usuarioActualizado = new UsuarioRegistroDTO(
                "nuevaPassword",
                "nuevoEmail@example.com",
                "USER",
                "NuevoNombre",
                "NuevoApellido",
                123456789,
                20L,
                10L
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String usuarioJson = objectMapper.writeValueAsString(usuarioActualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/auth/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isOk());

    }*/
/*
    @Test
    @Order(6)
    public void testEliminarUsuario() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/auth/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }*/
   }