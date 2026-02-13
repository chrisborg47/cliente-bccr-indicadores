package cr.ac.ulatinabccr.infraestructura.xml;

import cr.ac.ulatinabccr.modelo.IndicadorEconomico;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class IndicadoresXmlParserDomXPath implements IndicadoresXmlParser {

    @Override
    public List<IndicadorEconomico> parse(String xml, int codigoIndicador) throws Exception {
        List<IndicadorEconomico> lista = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false); // para el mock simple

        Document doc = dbf.newDocumentBuilder().parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))
        );

        NodeList registros = doc.getElementsByTagName("Registro");
        for (int i = 0; i < registros.getLength(); i++) {
            var registro = registros.item(i);

            // Buscar hijos por nombre
            String fecha = null;
            String valorStr = null;

            NodeList hijos = registro.getChildNodes();
            for (int j = 0; j < hijos.getLength(); j++) {
                var n = hijos.item(j);
                if (n.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) continue;

                switch (n.getNodeName()) {
                    case "Fecha" -> fecha = n.getTextContent().trim();
                    case "Valor" -> valorStr = n.getTextContent().trim();
                }
            }

            if (fecha != null && valorStr != null) {
                BigDecimal valor = new BigDecimal(valorStr);
                lista.add(new IndicadorEconomico(fecha, valor, codigoIndicador, null));
            }
        }

        return lista;
    }
}
