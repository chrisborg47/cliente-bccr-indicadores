package cr.ac.ulatinabccr.infraestructura.soap;

import cr.ac.ulatinabccr.modelo.ParametrosConsulta;

/**
 * Interfaz que define el contrato de comunicación con el servicio SOAP del BCCR.
 *
 * Esta interfaz pertenece a la capa de infraestructura, pero es utilizada
 * por la capa de negocio. La idea es que negocio dependa de una abstracción
 * y no de una implementación concreta (por ejemplo, JAX-WS o un Mock).
 *
 * Esto permite:
 * - Cambiar la tecnología SOAP sin afectar negocio.
 * - Usar un Mock para pruebas.
 * - Mantener bajo acoplamiento entre capas.
 */
public interface BccrSoapClient {

    /**
     * Ejecuta la operación SOAP:
     * ObtenerIndicadoresEconomicosXML
     *
     * Recibe un objeto ParametrosConsulta que encapsula los datos necesarios
     * para realizar la consulta (indicador, fechas, subniveles).
     *
     * Retorna el XML crudo como String tal cual lo envía el BCCR.
     * El procesamiento y extracción de datos no se hace aquí,
     * sino en la capa de parsing XML.
     *
     * @param params parámetros necesarios para realizar la consulta.
     * @return XML crudo devuelto por el servicio del BCCR.
     * @throws Exception si ocurre un error de conexión, SOAP o configuración.
     */
    String obtenerIndicadoresEconomicosXML(ParametrosConsulta params) throws Exception;
}
