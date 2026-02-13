package cr.ac.ulatinabccr.controlador;

import cr.ac.ulatinabccr.modelo.ParametrosConsulta;
import cr.ac.ulatinabccr.modelo.ResultadoConsulta;
import cr.ac.ulatinabccr.negocio.IndicadoresService;
import cr.ac.ulatinabccr.util.ValidadorFechas;

/**
 * Controlador/fachada: único punto de entrada que la GUI debe usar.
 * La GUI NO maneja SOAP/XML: solo llama este método y recibe un ResultadoConsulta.
 */
public class ConsultaIndicadoresController {

    private final IndicadoresService service;

    public ConsultaIndicadoresController(IndicadoresService service) {
        this.service = service;
    }

    /**
     * Contrato estable para integrar con la GUI.
     */
    public ResultadoConsulta consultar(int indicador, String fechaInicio, String fechaFinal, String subNiveles) {

        // Validaciones básicas (capa controlador)
        if (indicador <= 0) {
            return ResultadoConsulta.error("Indicador inválido.");
        }

        if (!ValidadorFechas.esFormatoValido(fechaInicio) || !ValidadorFechas.esFormatoValido(fechaFinal)) {
            return ResultadoConsulta.error("Formato de fecha inválido. Use dd/mm/yyyy.");
        }

        if (subNiveles == null || (!subNiveles.equalsIgnoreCase("S") && !subNiveles.equalsIgnoreCase("N"))) {
            return ResultadoConsulta.error("Subniveles inválido. Use 'S' o 'N'.");
        }

        ParametrosConsulta params = new ParametrosConsulta(
                indicador,
                fechaInicio.trim(),
                fechaFinal.trim(),
                subNiveles.toUpperCase()
        );

        // Delegación a capa de negocio
        return service.consultar(params);
    }
}
