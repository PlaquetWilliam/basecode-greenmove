package greenmove.infrastructure.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Fournit les connexions à la base H2 en mémoire. */
public final class ConnexionH2 {

    private static final String URL = "jdbc:h2:mem:greenmove;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private ConnexionH2() {
    }

    public static Connection ouvrir() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
