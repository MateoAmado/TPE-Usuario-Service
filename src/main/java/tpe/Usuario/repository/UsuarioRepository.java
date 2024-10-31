package tpe.Usuario.repository;


import tpe.Usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u WHERE u.email =:email")
    Usuario findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.password =:password")
    Usuario findByPassword(@Param("password") String password);
}