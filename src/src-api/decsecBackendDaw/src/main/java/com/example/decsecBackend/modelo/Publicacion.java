package com.example.decsecBackend.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Publicacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
	@ElementCollection(fetch = FetchType.LAZY, targetClass = Imagen.class)
	private List<Imagen> imagenes = new ArrayList<>();

	@NotNull
	private String comentarioUsuario;

	private LocalDateTime fechaPublicacion;

	@OneToMany(mappedBy = "publicacion", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@ElementCollection(fetch = FetchType.LAZY, targetClass = Comentario.class)
	@JsonBackReference
	private List<Comentario> comentarios = new ArrayList<>();

	@ManyToOne
	@ElementCollection(fetch = FetchType.EAGER, targetClass = Usuario.class)
	@JoinColumn(name = "usuario_id", nullable = false)
	@JsonManagedReference
	private Usuario usuario;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<Usuario> usuariosQueDieronMeGusta;

}
