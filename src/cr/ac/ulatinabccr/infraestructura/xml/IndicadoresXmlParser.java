package cr.ac.ulatinabccr.infraestructura.xml;

import cr.ac.ulatinabccr.modelo.IndicadorEconomico;
import java.util.List;

/**
 * Contrato para convertir el XML crudo del BCCR en objetos del modelo.
 */
public interface IndicadoresXmlParser {

    /**
     * Parsea el XML y retorna una lista de datos para el indicador solicitado.
     */
    List<IndicadorEconomico> parse(String xml, int codigoIndicador) throws Exception;
}
