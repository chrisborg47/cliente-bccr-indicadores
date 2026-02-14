package cr.ac.ulatinabccr.infraestructura.soap;

import cr.ac.ulatinabccr.config.BccrConfig;
import cr.ac.ulatinabccr.modelo.ParametrosConsulta;
import cr.ac.ulatinabccr.ws.Wsindicadoreseconomicos;
import cr.ac.ulatinabccr.ws.WsindicadoreseconomicosSoap;

public class BccrSoapClientJaxWs implements BccrSoapClient {

    private final BccrConfig config;

    public BccrSoapClientJaxWs(BccrConfig config) {
        this.config = config;
    }

    @Override
    public String obtenerIndicadoresEconomicosXML(ParametrosConsulta params) throws Exception {

        Wsindicadoreseconomicos service = new Wsindicadoreseconomicos();
        WsindicadoreseconomicosSoap port = service.getWsindicadoreseconomicosSoap();

        return port.obtenerIndicadoresEconomicosXML(
                String.valueOf(params.getIndicador()),
                params.getFechaInicio(),
                params.getFechaFinal(),
                config.getNombre(),
                params.getSubNiveles(),
                config.getCorreo(),
                config.getToken()
        );
    }
}
