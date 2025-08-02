package src.dao;

import src.db.Conexion;
import src.modelo.Comentario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO {

    public static void insertarComentario(Comentario comentario) {
        String sql = "INSERT INTO comentarios (contenido, usuario_id, post_id, fecha_creacion) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, comentario.getContenido());
            stmt.setInt(2, comentario.getUsuarioId());
            stmt.setInt(3, comentario.getPostId());
            stmt.setTimestamp(4, Timestamp.valueOf(comentario.getFechaCreacion()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Comentario> obtenerComentariosPorPost(int postId) {
        List<Comentario> comentarios = new ArrayList<>();
        String sql = """
    SELECT c.id, c.contenido, c.usuario_id, c.post_id, c.fecha_creacion, u.nombre AS nombre_autor, u.foto_perfil
    FROM comentarios c
    JOIN usuarios u ON c.usuario_id = u.id
    WHERE c.post_id = ?
    ORDER BY c.fecha_creacion ASC
    """;

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comentario comentario = new Comentario(
                        rs.getInt("id"),
                        rs.getString("contenido"),
                        rs.getInt("usuario_id"),
                        rs.getInt("post_id"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getString("nombre_autor"),
                        rs.getString("foto_perfil")
                );
                comentarios.add(comentario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comentarios;
    }


}
