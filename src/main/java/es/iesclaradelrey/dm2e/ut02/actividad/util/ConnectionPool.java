package es.iesclaradelrey.dm2e.ut02.actividad.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public enum ConnectionPool {
    INSTANCE;

    // Info BBDD
    private final String URL = "jdbc:postgresql://localhost:5432/chinook";
    private final String USER = "chinook";
    private final String PASSWORD = "chinook";

    // Recurso compartido
    private final HikariDataSource hds;

    // Constructor para inicializar la configuración
    ConnectionPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        hds = new HikariDataSource(config);
    }

    // Método para obtener una conexión del pool
    public Connection getConnection() throws SQLException {
        return hds.getConnection();
    }

    // Método para cerrar el pool previo a cerrar la app
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
