package cr.ac.ulatinabccr.infraestructura.soap;

import cr.ac.ulatinabccr.modelo.ParametrosConsulta;

/**
 * Contrato de infraestructura para obtener el XML crudo desde el BCCR.
 * Negocio usa esta interfaz, no implementaciones concretas.
 */
public interface BccrSoapClient {

    /**
     * Ejecuta la operación ObtenerIndicadoresEconomicosXML y retorna el XML como String.
     */
    String obtenerIndicadoresEconomicosXML(ParametrosConsulta params) throws Exception;
}
