package src.modelo;

import java.time.LocalDateTime;

public class Comentario {
    private int id;
    private String contenido;
    private int usuarioId;
    private int postId;
    private LocalDateTime fechaCreacion;
    private String nombreAutor;
    private String fotoPerfilAutor;

    // Constructor  con id
    public Comentario(int id, String contenido, int usuarioId, int postId, LocalDateTime fechaCreacion, String nombreAutor, String fotoPerfilAutor) {
        this.id = id;
        this.contenido = contenido;
        this.usuarioId = usuarioId;
        this.postId = postId;
        this.fechaCreacion = fechaCreacion;
        this.nombreAutor = nombreAutor;
        this.fotoPerfilAutor = fotoPerfilAutor;
    }

    // Constructor sin id
    public Comentario(String contenido, int usuarioId, int postId, LocalDateTime fechaCreacion) {
        this(-1, contenido, usuarioId, postId, fechaCreacion, null, null);
    }

    // Getters y setters, nomames no use el de id XDDDDDDDDDDDDDDDD

    public String getContenido() {
        return contenido;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getPostId() {
        return postId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public String getFotoPerfilAutor() {
        return fotoPerfilAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public void setFotoPerfilAutor(String fotoPerfilAutor) {
        this.fotoPerfilAutor = fotoPerfilAutor;
    }
}
