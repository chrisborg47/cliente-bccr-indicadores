package cr.ac.ulatinabccr.Main;

import cr.ac.ulatinabccr.controlador.ConsultaIndicadoresController;
import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClient;
import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClientMock;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParser;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParserDomXPath;
import cr.ac.ulatinabccr.negocio.IndicadoresService;
import cr.ac.ulatinabccr.negocio.IndicadoresServiceImpl;

public class Main {

    public static void main(String[] args) {

        // Infraestructura (por ahora mock + parser stub o mock-parser)
        BccrSoapClient soapClient = new BccrSoapClientMock();
        IndicadoresXmlParser parser = new IndicadoresXmlParserDomXPath();

        // Negocio
        IndicadoresService service = new IndicadoresServiceImpl(soapClient, parser);

        // Controlador (contrato con GUI)
        ConsultaIndicadoresController controller = new ConsultaIndicadoresController(service);

        // Prueba rápida por consola para validar que compila y el flujo responde
        var resultado = controller.consultar(317, "01/01/2024", "10/01/2024", "N");

        System.out.println("OK: " + resultado.isOk());
        System.out.println("Mensaje: " + resultado.getMensaje());
        System.out.println("Datos: " + resultado.getDatos().size());
        resultado.getDatos().forEach(d -> System.out.println(d));
    }
}
