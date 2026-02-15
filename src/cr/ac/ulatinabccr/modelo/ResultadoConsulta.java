package cr.ac.ulatinabccr.modelo;

import java.util.List;

/**
 * DTO (Data Transfer Object) que representa el resultado estándar
 * de una consulta al servicio del BCCR.
 *
 * Objetivo:
 * - Evitar que la GUI reciba excepciones directamente.
 * - Encapsular el estado de la operación (éxito o error),
 *   un mensaje descriptivo y los datos obtenidos.
 *
 * Diseño:
 * En lugar de lanzar errores hacia la capa de presentación,
 * se retorna un objeto ResultadoConsulta con ok=false y un mensaje.
 * Esto facilita mostrar mensajes controlados en la interfaz.
 */
public class ResultadoConsulta {

    /**
     * Indica si la operación fue exitosa (true) o falló (false).
     */
    private final boolean ok;

    /**
     * Mensaje descriptivo del resultado.
     * Puede ser:
     * - "Consulta exitosa."
     * - "Sin datos para el rango indicado."
     * - "Error consultando BCCR: ..."
     */
    private final String mensaje;

    /**
     * Lista de datos devueltos por la consulta.
     * Si ocurre un error o no hay datos, puede venir vacía.
     */
    private final List<IndicadorEconomico> datos;

    /**
     * Constructor principal.
     *
     * @param ok estado de la operación.
     * @param mensaje mensaje descriptivo.
     * @param datos lista de indicadores obtenidos.
     */
    public ResultadoConsulta(boolean ok, String mensaje, List<IndicadorEconomico> datos) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    /**
     * @return true si la consulta fue exitosa.
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @return mensaje asociado al resultado.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @return lista de datos (puede estar vacía).
     */
    public List<IndicadorEconomico> getDatos() {
        return datos;
    }

    /**
     * Método helper para construir un resultado exitoso.
     * Se usa en la capa de negocio para hacer el código más legible.
     *
     * Ejemplo:
     * return ResultadoConsulta.exito("Consulta exitosa.", lista);
     */
    public static ResultadoConsulta exito(String mensaje, List<IndicadorEconomico> datos) {
        return new ResultadoConsulta(true, mensaje, datos);
    }

    /**
     * Método helper para construir un resultado de error.
     * Retorna lista vacía para evitar nulls en la GUI.
     *
     * Ejemplo:
     * return ResultadoConsulta.error("Error de conexión");
     */
    public static ResultadoConsulta error(String mensaje) {
        return new ResultadoConsulta(false, mensaje, List.of());
    }
}


