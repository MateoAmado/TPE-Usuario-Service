package tpe.Usuario.services;

import tpe.Usuario.dto.UsuarioLoginDTO;
import tpe.Usuario.dto.UsuarioRegistroDTO;
import tpe.Usuario.model.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface DefaultUserService extends UserDetailsService {
	Usuario save(UsuarioRegistroDTO usuario_dto);



	UserDetails loadUserByUsername(String email);


}
