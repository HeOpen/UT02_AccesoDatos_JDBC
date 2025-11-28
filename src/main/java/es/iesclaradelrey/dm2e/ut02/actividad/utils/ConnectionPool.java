/**
 * Clase utilitaria que implementa el patrón Singleton mediante Enum para gestionar
 * un pool de conexiones a la base de datos usando HikariCP.
 * Proporciona una única instancia global para gestionar las conexiones de forma eficiente.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public enum ConnectionPool {

    /**
     * Instancia única del pool de conexiones (patrón Singleton).
     */
    INSTANCE;

    // Configuración de conexión a la base de datos
    // No Hardcodear -> usar variables de entorno (buenas prácticas)

    /**
     * URL de conexión a la base de datos.
     * Prioriza variables de entorno, usa valor por defecto si no está definida.
     */
    private final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/chinook");

    /**
     * Usuario para la conexión a la base de datos.
     * Prioriza variables de entorno, usa valor por defecto si no está definida.
     */
    private final String USER = System.getenv().getOrDefault("DB_USER", "chinook");

    /**
     * Contraseña para la conexión a la base de datos.
     * Prioriza variables de entorno, usa valor por defecto si no está definida.
     */
    private final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "chinook");

    /**
     * DataSource de HikariCP que gestiona el pool de conexiones.
     * Recurso compartido y thread-safe.
     */
    private final HikariDataSource hds;

    /**
     * Constructor del enum que inicializa la configuración del pool de conexiones.
     * Se ejecuta una única vez al cargar la clase.
     * Configura HikariCP con los parámetros de conexión a la base de datos.
     */
    ConnectionPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        hds = new HikariDataSource(config);
    }

    /**
     * Obtiene una conexión del pool de conexiones.
     *
     * @return Conexión a la base de datos
     * @throws SQLException Si ocurre un error al obtener la conexión
     */
    public Connection getConnection() throws SQLException {
        return hds.getConnection();
    }

    /**
     * Cierra el pool de conexiones de forma segura.
     * Debe ser llamado antes de finalizar la aplicación para liberar recursos.
     * Verifica que el pool exista y no esté ya cerrado antes de proceder.
     */
    public void closePool() {
        if (hds != null && !hds.isClosed()) {
            hds.close();
        }
    }
}