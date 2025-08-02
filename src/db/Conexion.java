// Clase para conectar a PostgreSQL
package src.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://mainline.proxy.rlwy.net:48019/railway";
    private static final String USER = "postgres";
    private static final String PASSWORD = "TyuVrjyAqjeMifwaVttvbiEHhffUnrYN";

   // private static Connection conexion;
    static {
       try {
           Class.forName("org.postgresql.Driver"); // carga el driver una vez al inicio
       } catch (ClassNotFoundException e) {
           System.err.println("No se encontró el driver de PostgreSQL.");
           e.printStackTrace();
       }
   }
    public static Connection getConexion() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
 /* if (conexion == null) {
            try {
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos.");
            } catch (ClassNotFoundException e) {
                System.err.println("No se encontró el driver de PostgreSQL.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos.");
                e.printStackTrace();
            }
        }
        return conexion;

        */
