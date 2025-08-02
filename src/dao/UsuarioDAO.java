package src.dao;

import src.db.Conexion;
import src.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public static Usuario buscarPorCorreoYContrasena(String correo, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("foto_perfil"),
                        rs.getString("biografia")
                );

                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario:");
            e.printStackTrace();
        }

        return null;
    }

    public static boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, foto_perfil, biografia) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContrasena());
            stmt.setString(4, usuario.getFotoPerfil() != null ? usuario.getFotoPerfil() : "default_profile.png");
            stmt.setString(5, usuario.getBiografia() != null ? usuario.getBiografia() : "");

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario:");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, foto_perfil = ?, biografia = ? WHERE id = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getFotoPerfil());
            stmt.setString(4, usuario.getBiografia());
            stmt.setInt(5, usuario.getId());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Usuario obtenerUsuarioPorId(int idUsuario) {
        String sql = "SELECT id, nombre, correo, contrasena, foto_perfil, biografia FROM usuarios WHERE id = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setFotoPerfil(rs.getString("foto_perfil"));
                usuario.setBiografia(rs.getString("biografia"));
                return usuario;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Si no encuentra el usuario
    }

}

