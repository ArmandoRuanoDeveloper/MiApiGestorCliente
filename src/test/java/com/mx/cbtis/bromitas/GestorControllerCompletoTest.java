package com.mx.cbtis.bromitas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.springframework.web.servlet.ModelAndView;

import com.mx.cbtis.controller.GestorController;
import com.mx.cbtis.modelo.Comentarios;
import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Tareas;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

class GestorControllerCompletoTest {

    static HttpServer backend;
    GestorController controller = new GestorController();

    @BeforeAll
    static void levantarBackendFalso() throws IOException {
        backend = HttpServer.create(new InetSocketAddress("localhost", 8081), 0);
        backend.createContext("/addProyecto", ex -> responder(ex, "1"));
        backend.createContext("/deleteProyecto", ex -> responder(ex, "1"));
        backend.createContext("/buscarUsuarios", ex -> responder(ex, "[{\"nombre\":\"Ana\"}]"));
        backend.createContext("/getTarea", ex -> responder(ex, "[{\"id_tarea\":1,\"nombre\":\"Tarea 1\"}]"));
        backend.createContext("/getComentario", ex -> responder(ex, "[{\"id_comentario\":1,\"texto_comentario\":\"Hola\"}]"));
        backend.createContext("/addTareas", ex -> responder(ex, "1"));
        backend.createContext("/addComentario", ex -> responder(ex, "1"));
        backend.createContext("/buscarTarea", ex -> responder(ex, "{\"id_tarea\":7,\"nombre\":\"Tarea 7\"}"));
        backend.createContext("/deleteTareas", ex -> responder(ex, "1"));
        backend.createContext("/buscarComentarios", ex -> responder(ex, "[{\"id_comentario\":1,\"texto_comentario\":\"Hola\"}]"));
        backend.createContext("/buscarTareas", ex -> responder(ex, "[{\"id_tarea\":1,\"nombre\":\"Tarea 1\"}]"));
        backend.start();
    }

    @AfterAll
    static void apagarBackendFalso() {
        backend.stop(0);
    }

    static void responder(HttpExchange ex, String json) throws IOException {
        ex.getRequestBody().readAllBytes();
        byte[] cuerpo = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(200, cuerpo.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(cuerpo);
        }
    }

    // Vistas

    @Test
    void vistasSimples_devuelvenElNombreDeLaVista() {
        assertEquals("home", controller.getHome());
        assertEquals("tutorial", controller.getTutorial());
        assertEquals("manual", controller.getManual());
        assertEquals("login", controller.getLogin());
    }

    @Test
    void getRegister_agregaUsuarioVacioAlModelo() {
        Model model = new ConcurrentModel();

        assertEquals("register", controller.getRegister(model));
        assertNotNull(model.getAttribute("usuario"));
    }

    @Test
    void getInicio_agregaProyectosVacioAlModelo() {
        Model model = new ConcurrentModel();

        assertEquals("inicio", controller.getInicio(model));
        assertNotNull(model.getAttribute("proyectos"));
    }

    @Test
    void getTarea_devuelveVistaProyectoConTareasEnElModelo() {
        Model model = new ConcurrentModel();

        assertEquals("proyecto", controller.getTarea(model));
        assertNotNull(model.getAttribute("tareas"));
    }

    // Proyectos

    @Test
    void setSaveProyecto_redirigeAInicio() {
        ModelAndView mav = controller.setSaveProyecto(new Proyectos());

        assertEquals("redirect:/inicio", mav.getViewName());
    }

    @Test
    void eliminarProyecto_redirigeAInicio() {
        Proyectos p = new Proyectos();
        p.setId_proyecto(5);

        ModelAndView mav = controller.eliminarProyecto(p);

        assertEquals("redirect:/inicio", mav.getViewName());
    }

    // Usuarios

    @Test
    void consultarUsuarios_devuelveListaYLaGuardaEnElModelo() {
        Model model = new ConcurrentModel();

        ResponseEntity<Object> r = controller.consultarUsuarios(model);

        assertEquals(200, r.getStatusCode().value());
        assertEquals(1, ((List<?>) r.getBody()).size());
        assertEquals(1, ((List<?>) model.getAttribute("listaUsuarios")).size());
    }

    // Tareas

    @Test
    void obtenerTareas_devuelveLasTareasDelProyecto() {
        Model model = new ConcurrentModel();
        Tareas filtro = new Tareas();
        filtro.setId_proyecto(10);

        ResponseEntity<List<Tareas>> r = controller.obtenerTareas(filtro, model);

        assertEquals(200, r.getStatusCode().value());
        assertEquals(1, r.getBody().size());
        assertEquals("Tarea 1", r.getBody().get(0).getNombre());
        assertNotNull(model.getAttribute("tareas"));
    }

    @Test
    void setSaveTareas_redirigeAProyectoConElIdDelProyecto() {
        Tareas t = new Tareas();
        t.setId_proyecto(5);

        ModelAndView mav = controller.setSaveTareas(t);

        assertEquals("redirect:/proyecto", mav.getViewName());
        assertEquals(5, mav.getModel().get("id_proyecto"));
    }

    @Test
    void updateTarea_devuelveLaTareaConsultada() {
        Tareas t = new Tareas();
        t.setId_tarea(7);

        ResponseEntity<Object> r = controller.updateTarea(t);

        assertEquals(200, r.getStatusCode().value());
        assertEquals(7, ((Tareas) r.getBody()).getId_tarea());
    }

    @Test
    void eliminarTarea_redirigeAProyecto() {
        Tareas t = new Tareas();
        t.setId_tarea(7);

        ModelAndView mav = controller.eliminarTarea(t);

        assertEquals("redirect:/proyecto", mav.getViewName());
    }

    @Test
    void verificarTarea_devuelve1CuandoElBackendGuarda() {
        ResponseEntity<Integer> r = controller.verificarTarea(new Tareas());

        assertEquals(200, r.getStatusCode().value());
        assertEquals(1, r.getBody());
    }

    @Test
    void cambiosTareas_devuelve1CuandoElBackendGuarda() {
        ResponseEntity<Integer> r = controller.cambiosTareas(new Tareas());

        assertEquals(200, r.getStatusCode().value());
        assertEquals(1, r.getBody());
    }

    // Comentarios

    @Test
    void obtenerComentarios_devuelveLosComentariosDeLaTarea() {
        Model model = new ConcurrentModel();
        Comentarios filtro = new Comentarios();
        filtro.setId_tarea(1);

        ResponseEntity<List<Comentarios>> r = controller.obtenerComentarios(filtro, model);

        assertEquals(200, r.getStatusCode().value());
        assertEquals("Hola", r.getBody().get(0).getTexto_comentario());
        assertNotNull(model.getAttribute("comentarios"));
    }

    @Test
    void setSaveComentario_devuelveLaVistaProyecto() {
        assertEquals("proyecto", controller.setSaveComentario(new Comentarios()));
    }

    @Test
    void consultarComentarios_devuelveLaLista() {
        ResponseEntity<Object> r = controller.consultarComentarios();

        assertEquals(200, r.getStatusCode().value());
        assertEquals(1, ((List<?>) r.getBody()).size());
    }

    @Test
    void consultarTareas_devuelveLaListaCuandoElBackendResponde() {
        ResponseEntity<Object> r = controller.consultarTareas();

        assertEquals(200, r.getStatusCode().value());
        assertEquals(1, ((List<?>) r.getBody()).size());
    }
}