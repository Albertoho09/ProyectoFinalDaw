package com.example.decsecBackend.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Usuario implements UserDetails {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nick;

	private String nombre;

	private String apellidos;

	private LocalDate fechaNac;

	private String password;

	private Boolean privado;

	@OneToOne
	private Imagen foto;

	@OneToOne
	private Imagen banner;

	@Column(unique = true)
	private String email;

	@ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "usuario_rol")
	@Column(name = "RolesUsuario")
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	@ElementCollection(fetch = FetchType.LAZY, targetClass = Publicacion.class)
	@JsonBackReference
	private List<Publicacion> publicaciones = new ArrayList<>();

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	@ElementCollection(fetch = FetchType.LAZY, targetClass = Comentario.class)
	@JsonBackReference
	private List<Comentario> comentarios = new ArrayList<>();

	@ManyToMany(mappedBy = "usuariosQueDieronMeGusta")
    private List<Publicacion> publicacionesConMeGusta;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Cargar la colección de roles de manera temprana
		roles.size(); // Esto carga la colección de roles

		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void asignarPublicacion(Publicacion publi) {
		this.publicaciones.add(publi);
	}

}
