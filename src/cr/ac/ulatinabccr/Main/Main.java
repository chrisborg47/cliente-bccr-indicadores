package cr.ac.ulatinabccr.Main;

import cr.ac.ulatinabccr.config.BccrConfig;
import cr.ac.ulatinabccr.controlador.ConsultaIndicadoresController;
import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClient;
import cr.ac.ulatinabccr.infraestructura.soap.BccrSoapClientJaxWs;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParser;
import cr.ac.ulatinabccr.infraestructura.xml.IndicadoresXmlParserDomXPath;
import cr.ac.ulatinabccr.negocio.IndicadoresService;
import cr.ac.ulatinabccr.negocio.IndicadoresServiceImpl;
import cr.ac.ulatinabccr.modelo.ParametrosConsulta;

public class Main {

    public static void main(String[] args) {

        try {

            // 🔹 1) Cargar configuración (nombre, correo, token)
            BccrConfig config = new BccrConfig("bccr.properties");

            // 🔹 2) Infraestructura real (SOAP + Parser)
            BccrSoapClient soapClient = new BccrSoapClientJaxWs(config);
            IndicadoresXmlParser parser = new IndicadoresXmlParserDomXPath();

            // 🔹 3) Capa de negocio
            IndicadoresService service = new IndicadoresServiceImpl(soapClient, parser);

            // 🔹 4) Controlador (contrato con GUI)
            ConsultaIndicadoresController controller = new ConsultaIndicadoresController(service);

            // 🔹 5) Prueba manual por consola
        ParametrosConsulta params = new ParametrosConsulta(
                    318,                // Tipo de cambio compra
                    "01/01/2024",       // Fecha inicio
                    "10/01/2024",       // Fecha final
                    "N"                 // Subniveles
            );

            var resultado = controller.consultar(
        params.getIndicador(),
        params.getFechaInicio(),
        params.getFechaFinal(),
        params.getSubNiveles()
);


            System.out.println("OK: " + resultado.isOk());
            System.out.println("Mensaje: " + resultado.getMensaje());
            System.out.println("Datos: " + resultado.getDatos().size());
            resultado.getDatos().forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
