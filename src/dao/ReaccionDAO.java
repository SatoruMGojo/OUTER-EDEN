package src.dao;

import src.db.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaccionDAO {

    // Insertar o actualizar una reacción
    public static void darReaccion(int usuarioId, int postId, String tipo) {
        String sql = """
                INSERT INTO reacciones (usuario_id, post_id, tipo)
                VALUES (?, ?, ?)
                ON CONFLICT (usuario_id, post_id)
                DO UPDATE SET tipo = EXCLUDED.tipo;
                """;

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setInt(2, postId);
            stmt.setString(3, tipo);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al dar reacción:");
            e.printStackTrace();
        }
    }

    // Contador de likes o dislikes de un post
    public static int contarReacciones(int postId, String tipo) {
        String sql = "SELECT COUNT(*) FROM reacciones WHERE post_id = ? AND tipo = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setString(2, tipo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar reacciones:");
            e.printStackTrace();
        }

        return 0;
    }

    // Obtener la reacción de un usuario sobre un post para saber si ya reaccionó
    public static String obtenerReaccion(int usuarioId, int postId) {
        String sql = "SELECT tipo FROM reacciones WHERE usuario_id = ? AND post_id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tipo"); // los tipos son like, dislike o null si no hay reacción
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void quitarReaccion(int usuarioId, int postId) {
        String sql = "DELETE FROM reacciones WHERE usuario_id = ? AND post_id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
