package cr.ac.ulatinabccr.UI;

import cr.ac.ulatinabccr.config.BccrConfig;
import cr.ac.ulatinabccr.controlador.ConsultaIndicadoresController;
import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClient;
import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClientJaxWs;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParser;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParserDomXPath;
import cr.ac.ulatinabccr.negocio.IndicadoresService;
import cr.ac.ulatinabccr.negocio.IndicadoresServiceImpl;

import javax.swing.SwingUtilities;

public class MainGUI {

    public static void main(String[] args) throws Exception {

        BccrConfig config = new BccrConfig("bccr.properties");
        BccrSoapClient soapClient = new BccrSoapClientJaxWs(config);
        IndicadoresXmlParser parser = new IndicadoresXmlParserDomXPath();
        IndicadoresService service = new IndicadoresServiceImpl(soapClient, parser);
        ConsultaIndicadoresController controller = new ConsultaIndicadoresController(service);

        SwingUtilities.invokeLater(() -> new ClienteIndicadoresFrame(controller).setVisible(true));
    }
}
