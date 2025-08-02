package src.modelo;

import java.time.LocalDateTime;

public class Post {
    private int id;           // ID del post en la DB
    private int idUsuario;    // ID del autor
    private String contenido;
    private LocalDateTime fechaHora;
    private String nombreAutor;  // para mostrar en la UI
    private String fotoPerfilAutor; // La ruta o URL de la imagen


    // Constructor completo
    public Post(int id, int idUsuario, String contenido, LocalDateTime fechaHora, String nombreAutor, String fotoPerfilAutor) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.nombreAutor = nombreAutor;
        this.fotoPerfilAutor = fotoPerfilAutor;
    }


    public Post() {
        //cosntructor vacio
    }

    // Getters y Setters

    public String getFotoPerfilAutor() {
        return fotoPerfilAutor;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

}
