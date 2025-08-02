package src.modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String fotoPerfil;
    private String biografia;
//para cuando ya este en la database ay mi gatito miau miau
    public Usuario(int id, String nombre, String correo, String contrasena, String fotoPerfil, String biografia) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fotoPerfil = fotoPerfil;
        this.biografia = biografia;

    }
    public Usuario() {} //constructor vacio para despues rellenarlo

    // los c o n s e g u i do res y dadores
    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

}
