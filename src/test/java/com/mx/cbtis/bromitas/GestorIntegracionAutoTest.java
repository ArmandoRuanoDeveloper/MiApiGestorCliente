package com.mx.cbtis.bromitas;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import com.mx.cbtis.controller.GestorController;
import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Usuario;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

class GestorIntegracionAutoTest {

    static HttpServer backend;
    GestorController controller = new GestorController();

    @BeforeAll
    static void levantarBackendFalso() throws IOException {
        backend = HttpServer.create(new InetSocketAddress("localhost", 8081), 0);
        backend.createContext("/buscarProyectos", ex -> responder(ex, 200, "[{\"id_proyecto\":1,\"nombre\":\"Proyecto Test\"}]"));
        backend.createContext("/buscarUsuarios", ex -> responder(ex, 200, "[{\"nombre\":\"Ana\"}]"));
        backend.createContext("/addUsuario", ex -> responder(ex, 200, "1"));
        backend.createContext("/getUsuarioLogin", ex -> responder(ex, 200, "{\"nombre\":\"000\"}"));
        backend.createContext("/buscarProyecto", ex -> responder(ex, 200, "{}"));
        backend.createContext("/buscarTareas", ex -> responder(ex, 500, "{\"error\":\"fallo\"}"));
        backend.start();
    }

    @AfterAll
    static void apagarBackendFalso() {
        backend.stop(0);
    }

    static void responder(HttpExchange ex, int codigo, String json) throws IOException {
        ex.getRequestBody().readAllBytes();
        byte[] cuerpo = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(codigo, cuerpo.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(cuerpo);
        }
    }

    // ÉXITO

    @Test
    void AUT_E01_consultaProyectos_backendValido() {
        ResponseEntity<Object> r = controller.consultarProyectos(new ConcurrentModel());

        assertEquals(200, r.getStatusCode().value());
        assertEquals(1, ((List<?>) r.getBody()).size());
    }

    @Test
    void AUT_E02_registroUsuario_datosValidos() {
        Usuario u = new Usuario();
        u.setNombre("Ana");
        u.setCorreo_electronico("ana@mail.com");
        u.setContrasena("1234");
        u.setRol("admin");

        ModelAndView mav = controller.setSaveUsuario(u);

        assertEquals("redirect:/login", mav.getViewName());
    }

    @Test
    void AUT_E03_vistaProyecto_muestraUsuarios() {
        Model model = new ConcurrentModel();

        String vista = controller.getProyecto(model);

        assertEquals("proyecto", vista);
        assertEquals(1, ((List<?>) model.getAttribute("listaUsuarios")).size());
    }

    // FRACASO

    @Test
    void AUT_F01_consultaTareas_backendDevuelveError() {
        assertThrows(HttpServerErrorException.class, () -> controller.consultarTareas());
    }

    @Test
    void AUT_F02_login_credencialesInvalidas() {
        Usuario u = new Usuario();
        u.setCorreo_electronico("ana@mail.com");
        u.setContrasena("mala");

        ResponseEntity<Usuario> r = controller.verificarUsuario(u);

        assertEquals("000", r.getBody().getNombre());
    }

    @Test
    void AUT_F03_proyectoInexistente_devuelveObjetoVacio() {
        Proyectos p = new Proyectos();
        p.setId_proyecto(999);

        ResponseEntity<Object> r = controller.updateProyecto(p);

        assertEquals(200, r.getStatusCode().value());
        assertNull(((Proyectos) r.getBody()).getId_proyecto());
    }
}