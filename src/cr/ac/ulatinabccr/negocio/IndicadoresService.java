package cr.ac.ulatinabccr.negocio;

import cr.ac.ulatinabccr.modelo.ParametrosConsulta;
import cr.ac.ulatinabccr.modelo.ResultadoConsulta;

/**
 * Capa de negocio: define el caso de uso de consulta.
 * El controlador depende de esta interfaz.
 */
public interface IndicadoresService {

    ResultadoConsulta consultar(ParametrosConsulta params);
}
