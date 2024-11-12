package tpe.Usuario.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Usuario")
@Data
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String password;
    private String email;
    private String apellido;
    private String nombre;
    private int nro_celular;
    private String rol;
    private Long longitud;
    private Long latitud;
}


