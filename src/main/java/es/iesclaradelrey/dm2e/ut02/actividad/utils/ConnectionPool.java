package es.iesclaradelrey.dm2e.ut02.actividad.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public enum ConnectionPool {
    INSTANCE;

    // Info BBDD
    // No Hardcodear -> usar variables de entorno (buenas prácticas)
    private final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/chinook");
    private final String USER = System.getenv().getOrDefault("DB_USER", "chinook");
    private final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "chinook");

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
    public void closePool() {
        if (hds != null && !hds.isClosed()) {
            hds.close();
        }
    }
}
