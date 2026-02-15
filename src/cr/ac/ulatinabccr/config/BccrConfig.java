package cr.ac.ulatinabccr.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase de configuración para el cliente BCCR.
 *
 * Responsabilidad:
 * - Leer desde un archivo .properties los datos sensibles necesarios
 *   para consumir el servicio del BCCR (nombre, correo y token).
 *
 * Decisión de diseño:
 * No se colocan estos valores directamente en el código fuente
 * para evitar exponer credenciales en el repositorio.
 * El archivo se mantiene local y se excluye mediante .gitignore.
 */
public class BccrConfig {

    // Datos requeridos por el servicio SOAP del BCCR
    private final String nombre;
    private final String correo;
    private final String token;

    /**
     * Constructor que recibe la ruta del archivo de configuración.
     *
     * @param rutaArchivo ruta local del archivo .properties.
     * @throws IOException si el archivo no existe o no puede leerse.
     */
    public BccrConfig(String rutaArchivo) throws IOException {

        Properties props = new Properties();

        // Se usa try-with-resources para cerrar automáticamente el FileInputStream
        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            props.load(fis);
        }

        /*
         * Se leen las propiedades usando las claves definidas:
         *
         * bccr.nombre=...
         * bccr.correo=...
         * bccr.token=...
         *
         * Si alguna propiedad no existe, retornará null.
         */
        this.nombre = props.getProperty("bccr.nombre");
        this.correo = props.getProperty("bccr.correo");
        this.token = props.getProperty("bccr.token");
    }

    /**
     * @return Nombre registrado para consumir el servicio.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return Correo registrado ante el BCCR.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @return Token de suscripción al servicio BCCR.
     */
    public String getToken() {
        return token;
    }
}
