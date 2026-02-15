package cr.ac.ulatinabccr.negocio;

import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClient;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParser;
import cr.ac.ulatinabccr.modelo.ParametrosConsulta;
import cr.ac.ulatinabccr.modelo.ResultadoConsulta;

/**
 * Implementación de la capa de negocio para consultar indicadores económicos del BCCR.
 *
 * Responsabilidad de esta clase:
 * - Orquestar el flujo de la consulta (sin depender de GUI).
 * - Pedir el XML a la capa de infraestructura (SOAP).
 * - Enviar ese XML al parser para convertirlo a objetos de dominio (IndicadorEconomico).
 * - Devolver un ResultadoConsulta pensado para ser consumido por el controlador y la GUI.
 *
 * Esta clase NO construye el SOAP, NO parsea XML directamente y NO imprime resultados.
 * Solo coordina y aplica reglas simples (ej: "si la lista está vacía, mensaje de sin datos").
 */
public class IndicadoresServiceImpl implements IndicadoresService {

    /**
     * Cliente SOAP (infraestructura).
     * Se inyecta como interfaz para no acoplar negocio a una implementación específica
     * (JAX-WS real o mock para pruebas).
     */
    private final BccrSoapClient soapClient;

    /**
     * Parser de XML (infraestructura).
     * Convierte el XML del BCCR a una lista de objetos IndicadorEconomico.
     */
    private final IndicadoresXmlParser parser;

    /**
     * Constructor con inyección de dependencias.
     * Esto facilita pruebas (por ejemplo usando mocks) y mantiene el código desacoplado.
     */
    public IndicadoresServiceImpl(BccrSoapClient soapClient, IndicadoresXmlParser parser) {
        this.soapClient = soapClient;
        this.parser = parser;
    }

    /**
     * Ejecuta la consulta completa del indicador:
     * 1) Llama al BCCR para obtener el XML crudo.
     * 2) Parsea el XML y obtiene la lista de resultados.
     * 3) Retorna un ResultadoConsulta con estado, mensaje y datos.
     *
     * @param params parámetros de la consulta (indicador, fechas, subniveles).
     * @return ResultadoConsulta listo para ser consumido por el controlador/GUI.
     */
    @Override
    public ResultadoConsulta consultar(ParametrosConsulta params) {
        try {
            // 1) Obtener XML crudo desde el BCCR (SOAP real)
            String xml = soapClient.obtenerIndicadoresEconomicosXML(params);

            // 2) Convertir el XML a objetos (IndicadorEconomico)
            var lista = parser.parse(xml, params.getIndicador());

            // 3) Regla de negocio simple: si no hay registros, se reporta como "sin datos"
            if (lista.isEmpty()) {
                return ResultadoConsulta.exito("Sin datos para el rango indicado.", lista);
            }

            // Si hay datos, se reporta como consulta exitosa
            return ResultadoConsulta.exito("Consulta exitosa.", lista);

        } catch (Exception ex) {
            /*
             * Manejo de errores:
             * La capa de negocio no "lanza" la excepción hacia la GUI.
             * En lugar de eso, devuelve un ResultadoConsulta de error,
             * para que la interfaz pueda mostrar un mensaje controlado.
             *
             * Ejemplos de errores posibles:
             * - Token inválido
             * - Error de conexión / timeout
             * - XML inesperado
             */
            return ResultadoConsulta.error("Error consultando BCCR: " + ex.getMessage());
        }
    }
}
