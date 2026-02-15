package cr.ac.ulatinabccr.infraestructura.soap;

import cr.ac.ulatinabccr.config.BccrConfig;
import cr.ac.ulatinabccr.modelo.ParametrosConsulta;
import cr.ac.ulatinabccr.ws.Wsindicadoreseconomicos;
import cr.ac.ulatinabccr.ws.WsindicadoreseconomicosSoap;

/**
 * Implementación concreta del contrato BccrSoapClient utilizando JAX-WS.
 *
 * Esta clase pertenece a la capa de infraestructura y es responsable
 * de comunicarse directamente con el Web Service SOAP del BCCR.
 *
 * Utiliza las clases generadas automáticamente a partir del WSDL
 * (Wsindicadoreseconomicos y WsindicadoreseconomicosSoap).
 *
 * No contiene lógica de negocio ni procesamiento XML.
 * Su única responsabilidad es:
 * 1) Construir la llamada SOAP.
 * 2) Enviar los parámetros requeridos.
 * 3) Retornar el XML crudo como String.
 */
public class BccrSoapClientJaxWs implements BccrSoapClient {

    /**
     * Configuración externa del sistema.
     * Contiene datos institucionales como:
     * - Nombre del solicitante
     * - Correo electrónico
     * - Token de suscripción
     *
     * Se inyecta por constructor para mantener bajo acoplamiento
     * y facilitar pruebas o cambios futuros.
     */
    private final BccrConfig config;

    /**
     * Constructor que recibe la configuración necesaria para
     * autenticarse ante el servicio del BCCR.
     */
    public BccrSoapClientJaxWs(BccrConfig config) {
        this.config = config;
    }

    /**
     * Ejecuta la operación SOAP:
     * ObtenerIndicadoresEconomicosXML
     *
     * Flujo interno:
     * 1) Se instancia el objeto Service (Wsindicadoreseconomicos).
     * 2) Se obtiene el puerto SOAP (port).
     * 3) Se invoca el método generado por JAX-WS.
     *
     * Nota importante:
     * El servicio espera todos los parámetros como String,
     * incluso el indicador, por eso se convierte con String.valueOf().
     *
     * Los datos institucionales (nombre, correo, token) se obtienen
     * desde BccrConfig y no desde la interfaz gráfica.
     *
     * @param params objeto que encapsula indicador, fechas y subniveles.
     * @return XML crudo devuelto por el BCCR.
     * @throws Exception si ocurre error de conexión o ejecución SOAP.
     */
    @Override
    public String obtenerIndicadoresEconomicosXML(ParametrosConsulta params) throws Exception {

        // 1) Crear instancia del servicio generado desde el WSDL
        Wsindicadoreseconomicos service = new Wsindicadoreseconomicos();

        // 2) Obtener el puerto SOAP (endpoint real del BCCR)
        WsindicadoreseconomicosSoap port = service.getWsindicadoreseconomicosSoap();

        // 3) Invocar la operación SOAP que devuelve el XML
        return port.obtenerIndicadoresEconomicosXML(
                String.valueOf(params.getIndicador()), // indicador como String
                params.getFechaInicio(),               // formato dd/MM/yyyy
                params.getFechaFinal(),                // formato dd/MM/yyyy
                config.getNombre(),                   // nombre registrado
                params.getSubNiveles(),                // "S" o "N"
                config.getCorreo(),                   // correo registrado
                config.getToken()                     // token de suscripción
        );
    }
}
