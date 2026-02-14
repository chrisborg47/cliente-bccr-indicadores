package cr.ac.ulatinabccr.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BccrConfig {
    private final String nombre;
    private final String correo;
    private final String token;

    public BccrConfig(String rutaArchivo) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            props.load(fis);
        }
        this.nombre = props.getProperty("bccr.nombre");
        this.correo = props.getProperty("bccr.correo");
        this.token = props.getProperty("bccr.token");
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getToken() { return token; }
}
