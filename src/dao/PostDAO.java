package src.dao;

import src.modelo.Post;
import src.db.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    public static List<Post> obtenerTodosLosPosts() {
        List<Post> lista = new ArrayList<>();

        String sql = """
            SELECT p.id, p.contenido, p.fecha_creacion, p.usuario_id, u.nombre, u.foto_perfil
            FROM posts p
            JOIN usuarios u ON p.usuario_id = u.id
            ORDER BY p.fecha_creacion DESC
            """;

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("contenido"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getString("nombre"),
                        rs.getString("foto_perfil")
                );
                lista.add(post);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener posts: " + e.getMessage());
        }

        return lista;
    }

    public static List<Post> obtenerPostsPorUsuario(int usuarioId) {
        List<Post> posts = new ArrayList<>();
        String sql = """
            SELECT p.id, p.contenido, p.usuario_id, p.fecha_creacion, u.nombre AS nombre_autor, u.foto_perfil
            FROM posts p
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE p.usuario_id = ?
            ORDER BY p.fecha_creacion DESC
        """;

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("contenido"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getString("nombre_autor"),
                        rs.getString("foto_perfil")
                );
                posts.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public static boolean insertarPost(Post post) {
        String sql = "INSERT INTO posts (usuario_id, contenido, fecha_creacion) VALUES (?, ?, ?)";

        if (post.getFechaHora() == null) {
            post.setFechaHora(LocalDateTime.now());
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, post.getIdUsuario());
            stmt.setString(2, post.getContenido());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(post.getFechaHora()));

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al insertar post: " + e.getMessage());
            return false;
        }
    }

    public static List<Post> obtenerPostsLikeadosPorUsuario(int usuarioId) {
        List<Post> posts = new ArrayList<>();
        String sql = """
            SELECT p.id, p.usuario_id, p.contenido, p.fecha_creacion, u.nombre AS nombre_autor, u.foto_perfil
            FROM posts p
            JOIN reacciones r ON p.id = r.post_id
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE r.usuario_id = ? AND r.tipo = 'like'
        """;

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("contenido"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getString("nombre_autor"),
                        rs.getString("foto_perfil")
                );
                posts.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
