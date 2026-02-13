package cr.ac.ulatinabccr.negocio;

import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClient;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParser;
import cr.ac.ulatinabccr.modelo.ParametrosConsulta;
import cr.ac.ulatinabccr.modelo.ResultadoConsulta;

public class IndicadoresServiceImpl implements IndicadoresService {

    private final BccrSoapClient soapClient;
    private final IndicadoresXmlParser parser;

    public IndicadoresServiceImpl(BccrSoapClient soapClient, IndicadoresXmlParser parser) {
        this.soapClient = soapClient;
        this.parser = parser;
    }

    @Override
    public ResultadoConsulta consultar(ParametrosConsulta params) {
        try {
            String xml = soapClient.obtenerIndicadoresEconomicosXML(params);

            var lista = parser.parse(xml, params.getIndicador());

            if (lista.isEmpty()) {
                return ResultadoConsulta.exito("Sin datos para el rango indicado.", lista);
            }

            return ResultadoConsulta.exito("Consulta exitosa.", lista);

        } catch (Exception ex) {
            return ResultadoConsulta.error("Error consultando BCCR: " + ex.getMessage());
        }
    }
}
