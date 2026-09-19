package umg.edu.gt.gestionempleados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/prog2_db";

    private static final String USUARIO = "root";

    private static final String PASSWORD = "Lessen08";

    public static Connection conectar() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
        );
    }
}