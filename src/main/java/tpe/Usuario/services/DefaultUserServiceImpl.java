package tpe.Usuario.services;

import tpe.Usuario.dto.*;
import tpe.Usuario.dto.UsuarioLoginDTO;
import tpe.Usuario.model.Usuario;
import tpe.Usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Service
public class DefaultUserServiceImpl implements DefaultUserService {

	@Autowired
	UsuarioRepository usuario_repository;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuario_repository.findByEmail(username);

		return new org.springframework.security.core.userdetails.User(
				usuario.getEmail(),
				usuario.getPassword(),
				mapRolesToAuthorities(usuario.getRol())
		);
	}
	public Usuario findById(int id) {
		return usuario_repository.findById(id);
	}

	public Collection<? extends GrantedAuthority> mapRolesToAuthorities(String rol) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority(rol));
		return authorities;
	}

	@Override
	public Usuario save(UsuarioRegistroDTO usuario_dto) {
		String role = "USER";
		if (usuario_dto.getRol().equals("ADMIN"))
			role = "ADMIN";
		if(usuario_dto.getRol().equals("ENCARGADO")){
			role="ENCARGADO";
		}
		Usuario usuario = new Usuario();
		usuario.setEmail(usuario_dto.getEmail());
		usuario.setPassword(passwordEncoder.encode(usuario_dto.getPassword()));
		usuario.setNombre(usuario_dto.getNombre());
		usuario.setApellido(usuario_dto.getApellido());
		usuario.setNro_celular(usuario_dto.getNro_celular());
		usuario.setRol(role);

		return this.usuario_repository.save(usuario);
	}

	public void delete(Usuario usuario){
		if(usuario != null) {
			usuario_repository.delete(usuario);
		}
	}

	public Usuario update(Usuario usuarioEncontrado, Usuario usuario){
		if(usuario.getEmail() != null) {
			usuarioEncontrado.setEmail(usuario.getEmail());
		}
		if(usuario.getApellido() != null) {
			usuarioEncontrado.setApellido(usuario.getApellido());
		}
		if(usuario.getNombre() != null) {
			usuarioEncontrado.setNombre(usuario.getNombre());
		}
		if(usuario.getPassword() != null) {
			usuarioEncontrado.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
		if(usuario.getNro_celular() != usuarioEncontrado.getNro_celular()) {
			usuarioEncontrado.setNro_celular(usuario.getNro_celular());
		}

		usuario_repository.save(usuarioEncontrado);
		return usuarioEncontrado;
	}
}
