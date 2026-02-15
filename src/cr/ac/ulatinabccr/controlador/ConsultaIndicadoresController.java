package cr.ac.ulatinabccr.controlador;

import cr.ac.ulatinabccr.modelo.ParametrosConsulta;
import cr.ac.ulatinabccr.modelo.ResultadoConsulta;
import cr.ac.ulatinabccr.negocio.IndicadoresService;
import cr.ac.ulatinabccr.util.ValidadorFechas;

/**
 * Controlador (o fachada) del sistema.
 *
 * Es el único punto de entrada que debe utilizar la interfaz gráfica (GUI).
 *
 * Responsabilidades:
 * - Validar datos básicos ingresados por el usuario.
 * - Construir el objeto ParametrosConsulta.
 * - Delegar la lógica real a la capa de negocio.
 *
 * Importante:
 * Esta clase NO maneja SOAP, NO parsea XML y NO accede directamente
 * a infraestructura. Solo coordina y valida.
 */
public class ConsultaIndicadoresController {

    /**
     * Servicio de negocio que contiene la lógica principal de consulta.
     * Se inyecta por constructor para mantener bajo acoplamiento.
     */
    private final IndicadoresService service;

    /**
     * Constructor con inyección de dependencia.
     * Permite desacoplar el controlador de una implementación específica.
     */
    public ConsultaIndicadoresController(IndicadoresService service) {
        this.service = service;
    }

    /**
     * Método que será utilizado por la GUI para realizar consultas.
     *
     * Flujo:
     * 1) Validar datos de entrada.
     * 2) Construir objeto ParametrosConsulta.
     * 3) Delegar a la capa de negocio.
     *
     * @param indicador código del indicador (ej: 317 o 318).
     * @param fechaInicio fecha inicial en formato dd/mm/yyyy.
     * @param fechaFinal fecha final en formato dd/mm/yyyy.
     * @param subNiveles "S" o "N".
     * @return ResultadoConsulta con estado, mensaje y lista de datos.
     */
    public ResultadoConsulta consultar(int indicador, String fechaInicio, String fechaFinal, String subNiveles) {

        // Validación 1: indicador debe ser positivo
        if (indicador <= 0) {
            return ResultadoConsulta.error("Indicador inválido.");
        }

        // Validación 2: formato de fechas (dd/mm/yyyy)
        if (!ValidadorFechas.esFormatoValido(fechaInicio) ||
            !ValidadorFechas.esFormatoValido(fechaFinal)) {

            return ResultadoConsulta.error("Formato de fecha inválido. Use dd/mm/yyyy.");
        }

        // Validación 3: subniveles solo puede ser "S" o "N"
        if (subNiveles == null ||
            (!subNiveles.equalsIgnoreCase("S") &&
             !subNiveles.equalsIgnoreCase("N"))) {

            return ResultadoConsulta.error("Subniveles inválido. Use 'S' o 'N'.");
        }

        /*
         * Si todas las validaciones pasan,
         * se construye el DTO ParametrosConsulta.
         *
         * Se limpian espacios y se normaliza subNiveles a mayúscula.
         */
        ParametrosConsulta params = new ParametrosConsulta(
                indicador,
                fechaInicio.trim(),
                fechaFinal.trim(),
                subNiveles.toUpperCase()
        );

        // Delegación completa a la capa de negocio.
        return service.consultar(params);
    }
}
