package com.example.decsecBackend.configuracion;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.PublicacionRepositorio;
import com.example.decsecBackend.modelo.Publicacion;
import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;

@Component
public class InicializarDatos implements CommandLineRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioServicioImpl usuarioservicio;
    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Override
    public void run(String... args) throws Exception {
        try {

            Usuario usu1 = new Usuario();
            usu1.setNick("Raikeor");
            usu1.setNombre("Alberto");
            usu1.setApellidos("Hidalgo");
            usu1.setFechaNac(LocalDate.now());
            usu1.setEmail("correo@gmail.com");
            usu1.setPassword(passwordEncoder.encode("melva"));
            usu1.setPrivado(false);
            usu1.getRoles().add(Role.ROLE_ADMIN);

            usuarioservicio.crearUsuario(usu1);

            Publicacion publi1 = new Publicacion();
            publi1.setFechaPublicacion(LocalDate.now());
            publi1.setComentarioUsuario("Foto de Andres 1");

            Publicacion publi2 = new Publicacion();
            publi2.setFechaPublicacion(LocalDate.now());
            publi2.setComentarioUsuario("Foto de Andres 2");

            Usuario usu2 = new Usuario();
            usu2.setNick("Revived");
            usu2.setNombre("Andrés");
            usu2.setApellidos("Dominguez");
            usu2.setFechaNac(LocalDate.now());
            usu2.setEmail("otrocorreo@gmail.com");
            usu2.setPassword(passwordEncoder.encode("tomate"));
            usu2.setPrivado(false);
            usu2.getRoles().add(Role.ROLE_USER);

            publi1.setUsuario(usu2);
            publi2.setUsuario(usu2);

            usuarioservicio.crearUsuario(usu2);
            publicacionRepositorio.save(publi1);
            publicacionRepositorio.save(publi2);

            Publicacion publi3 = new Publicacion();
            publi3.setFechaPublicacion(LocalDate.now());
            publi3.setComentarioUsuario("Foto de Ana 1");

            Publicacion publi4 = new Publicacion();
            publi4.setFechaPublicacion(LocalDate.now());
            publi4.setComentarioUsuario("Foto de Ana 2");

            Usuario usu3 = new Usuario();
            usu3.setNick("Melva");
            usu3.setNombre("Ana");
            usu3.setApellidos("Cabesita");
            usu3.setFechaNac(LocalDate.now());
            usu3.setEmail("ana@gmail.com");
            usu3.setPassword(passwordEncoder.encode("tomate"));
            usu3.setPrivado(true);
            usu3.getRoles().add(Role.ROLE_USER);
            usuarioservicio.crearUsuario(usu3);

            publi3.setUsuario(usu3);
            publi4.setUsuario(usu3);

            publicacionRepositorio.save(publi3);
            publicacionRepositorio.save(publi4);

            Publicacion publi5 = new Publicacion();
            publi5.setFechaPublicacion(LocalDate.now());
            publi5.setComentarioUsuario("Foto de Antonio 1");

            Publicacion publi6 = new Publicacion();
            publi6.setFechaPublicacion(LocalDate.now());
            publi6.setComentarioUsuario("Foto de Antonio 2");

            Publicacion publi7 = new Publicacion();
            publi7.setFechaPublicacion(LocalDate.now());
            publi7.setComentarioUsuario("Foto de Antonio 3");

            Publicacion publi8 = new Publicacion();
            publi8.setFechaPublicacion(LocalDate.now());
            publi8.setComentarioUsuario("Foto de Antonio 4");

            Usuario usu4 = new Usuario();
            usu4.setNick("Preaching");
            usu4.setNombre("Antonio");
            usu4.setApellidos("Perez");
            usu4.setFechaNac(LocalDate.now());
            usu4.setEmail("albertoho09@gmail.com");
            usu4.setPassword(passwordEncoder.encode("cabeson"));
            usu4.setPrivado(false);
            usu4.getRoles().add(Role.ROLE_USER);
            usuarioservicio.crearUsuario(usu4);

            publi5.setUsuario(usu4);
            publi6.setUsuario(usu4);
            publi7.setUsuario(usu4);
            publi8.setUsuario(usu4);

            publicacionRepositorio.save(publi5);
            publicacionRepositorio.save(publi6);
            publicacionRepositorio.save(publi7);
            publicacionRepositorio.save(publi8);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
