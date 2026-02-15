package cr.ac.ulatinabccr.infraestructura.soap;

import cr.ac.ulatinabccr.config.BccrConfig;
import cr.ac.ulatinabccr.modelo.ParametrosConsulta;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;

public class BccrSoapClientSaaj implements BccrSoapClient {

    private static final String ENDPOINT =
            "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx";

    private static final String NAMESPACE = "http://ws.sdde.bccr.fi.cr";

    private final BccrConfig config;

    public BccrSoapClientSaaj(BccrConfig config) {
        this.config = config;
    }

    @Override
    public String obtenerIndicadoresEconomicosXML(ParametrosConsulta params) throws Exception {

        // 1️⃣ Crear mensaje SOAP
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();

        // 2️⃣ Agregar operación
        SOAPElement obtener = body.addChildElement(
                "ObtenerIndicadoresEconomicosXML",
                "",
                NAMESPACE
        );

        // 3️⃣ Agregar parámetros obligatorios
        obtener.addChildElement("Indicador")
                .addTextNode(String.valueOf(params.getIndicador()));

        obtener.addChildElement("FechaInicio")
                .addTextNode(params.getFechaInicio());

        obtener.addChildElement("FechaFinal")
                .addTextNode(params.getFechaFinal());

        obtener.addChildElement("Nombre")
                .addTextNode(config.getNombre());

        obtener.addChildElement("SubNiveles")
                .addTextNode(params.getSubNiveles());

        obtener.addChildElement("CorreoElectronico")
                .addTextNode(config.getCorreo());

        obtener.addChildElement("Token")
                .addTextNode(config.getToken());

        soapMessage.saveChanges();

        // 4️⃣ Enviar petición
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        SOAPMessage response = soapConnection.call(soapMessage, ENDPOINT);

        soapConnection.close();

        // 5️⃣ Convertir respuesta a String
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.writeTo(out);

        return out.toString();
    }
}
