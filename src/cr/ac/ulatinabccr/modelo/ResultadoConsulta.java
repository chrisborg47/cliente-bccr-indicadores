package cr.ac.ulatinabccr.modelo;

import java.util.List;

/**
 * DTO: resultado estándar para devolver a la capa de presentación.
 * La GUI nunca recibe exceptions: recibe un ResultadoConsulta con ok=false y mensaje.
 */
public class ResultadoConsulta {

    private final boolean ok;
    private final String mensaje;
    private final List<IndicadorEconomico> datos;

    public ResultadoConsulta(boolean ok, String mensaje, List<IndicadorEconomico> datos) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<IndicadorEconomico> getDatos() {
        return datos;
    }

    // Helpers para que el código quede más limpio en el Controller/Service
    public static ResultadoConsulta exito(String mensaje, List<IndicadorEconomico> datos) {
        return new ResultadoConsulta(true, mensaje, datos);
    }

    public static ResultadoConsulta error(String mensaje) {
        return new ResultadoConsulta(false, mensaje, List.of());
    }
}

