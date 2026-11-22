package com.mx.cbtis.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ModeloTest {

    @Test
    void asignaciones_constructoresYAccesores() {
        Asignaciones a = new Asignaciones(1, 2, 3, "2024-06-01");
        assertEquals(1, a.getId_asignacion());
        assertEquals(2, a.getId_usuario());
        assertEquals(3, a.getId_tarea());
        assertEquals("2024-06-01", a.getFecha_asignacion());

        Asignaciones b = new Asignaciones();
        b.setId_asignacion(10);
        b.setId_usuario(20);
        b.setId_tarea(30);
        b.setFecha_asignacion("2024-07-01");
        assertEquals(10, b.getId_asignacion());
        assertEquals(20, b.getId_usuario());
        assertEquals(30, b.getId_tarea());
        assertEquals("2024-07-01", b.getFecha_asignacion());
    }

    @Test
    void tareas_constructoresYAccesores() {
        Tareas t = new Tareas(1, "Base de datos", "Crear tablas", "2024-06-13", "terminado", "danger", 197);
        assertEquals(1, t.getId_tarea());
        assertEquals("Base de datos", t.getNombre());
        assertEquals("Crear tablas", t.getDescripcion());
        assertEquals("2024-06-13", t.getFecha_vencimiento());
        assertEquals("terminado", t.getLista());
        assertEquals("danger", t.getEstado());
        assertEquals(197, t.getId_proyecto());

        Tareas u = new Tareas();
        u.setId_tarea(2);
        u.setNombre("Pruebas");
        u.setDescripcion("Escribir pruebas");
        u.setFecha_vencimiento("2024-06-20");
        u.setLista("pendiente");
        u.setEstado("warning");
        u.setId_proyecto(198);
        assertEquals(2, u.getId_tarea());
        assertEquals("Pruebas", u.getNombre());
        assertEquals("Escribir pruebas", u.getDescripcion());
        assertEquals("2024-06-20", u.getFecha_vencimiento());
        assertEquals("pendiente", u.getLista());
        assertEquals("warning", u.getEstado());
        assertEquals(198, u.getId_proyecto());
    }

    @Test
    void comentarios_constructoresYAccesores() {
        Comentarios c = new Comentarios(1, "Ana", 199, "Subirlo ya", "2024-06-26 13:48:00");
        assertEquals(1, c.getId_comentario());
        assertEquals("Ana", c.getId_usuario());
        assertEquals(199, c.getId_tarea());
        assertEquals("Subirlo ya", c.getTexto_comentario());
        assertEquals("2024-06-26 13:48:00", c.getFecha_hora());

        Comentarios d = new Comentarios();
        d.setId_comentario(2);
        d.setId_usuario("Liliana");
        d.setId_tarea(200);
        d.setTexto_comentario("hola");
        d.setFecha_hora("2024-06-26 13:49:00");
        assertEquals(2, d.getId_comentario());
        assertEquals("Liliana", d.getId_usuario());
        assertEquals(200, d.getId_tarea());
        assertEquals("hola", d.getTexto_comentario());
        assertEquals("2024-06-26 13:49:00", d.getFecha_hora());
    }

    @Test
    void proyectos_constructoresYAccesores() {
        Proyectos p = new Proyectos(1, "My API Rest", "Api con 5 tablas", "2024-05-31", "2024-06-29", "En proceso", "/Images/a.gif");
        assertEquals(1, p.getId_proyecto());
        assertEquals("My API Rest", p.getNombre());
        assertEquals("Api con 5 tablas", p.getDescripcion());
        assertEquals("2024-05-31", p.getFecha_inicio());
        assertEquals("2024-06-29", p.getFecha_finalizacion());
        assertEquals("En proceso", p.getEstado_proyecto());
        assertEquals("/Images/a.gif", p.getFondo());

        Proyectos q = new Proyectos();
        q.setId_proyecto(2);
        q.setNombre("Otro");
        q.setDescripcion("Descripcion");
        q.setFecha_inicio("2024-01-01");
        q.setFecha_finalizacion("2024-02-01");
        q.setEstado_proyecto("Terminado");
        q.setFondo("/Images/b.gif");
        assertEquals(2, q.getId_proyecto());
        assertEquals("Otro", q.getNombre());
        assertEquals("Descripcion", q.getDescripcion());
        assertEquals("2024-01-01", q.getFecha_inicio());
        assertEquals("2024-02-01", q.getFecha_finalizacion());
        assertEquals("Terminado", q.getEstado_proyecto());
        assertEquals("/Images/b.gif", q.getFondo());
    }

    @Test
    void usuario_constructoresYAccesores() {
        Usuario u = new Usuario(1, "Ana", "ana@mail.com", "1234", "Estudiante", "#70FAB7");
        assertEquals(1, u.getId_usuario());
        assertEquals("Ana", u.getNombre());
        assertEquals("ana@mail.com", u.getCorreo_electronico());
        assertEquals("1234", u.getContrasena());
        assertEquals("Estudiante", u.getRol());
        assertEquals("#70FAB7", u.getImagen_usuario());

        Usuario v = new Usuario();
        v.setId_usuario(2);
        v.setNombre("Liliana");
        v.setCorreo_electronico("lili@mail.com");
        v.setContrasena("abcd");
        v.setRol("Admin");
        v.setImagen_usuario("#54EA01");
        assertEquals(2, v.getId_usuario());
        assertEquals("Liliana", v.getNombre());
        assertEquals("lili@mail.com", v.getCorreo_electronico());
        assertEquals("abcd", v.getContrasena());
        assertEquals("Admin", v.getRol());
        assertEquals("#54EA01", v.getImagen_usuario());
    }
}