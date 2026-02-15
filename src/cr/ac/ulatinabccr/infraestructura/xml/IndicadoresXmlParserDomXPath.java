package cr.ac.ulatinabccr.infraestructura.xml;

import cr.ac.ulatinabccr.modelo.IndicadorEconomico;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class IndicadoresXmlParserDomXPath implements IndicadoresXmlParser {

    @Override
    public List<IndicadorEconomico> parse(String xml, int codigoIndicador) throws Exception {
        List<IndicadorEconomico> lista = new ArrayList<>();

        if (xml == null || xml.isBlank()) {
            return lista;
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false); // tu XML real no trae xmlns en lo que imprimiste

        Document doc = dbf.newDocumentBuilder().parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))
        );

        // ✅ XML REAL del BCCR: INGC011_CAT_INDICADORECONOMIC repetido
        NodeList registros = doc.getElementsByTagName("INGC011_CAT_INDICADORECONOMIC");

        for (int i = 0; i < registros.getLength(); i++) {
            Element registro = (Element) registros.item(i);

            // Hijos directos
            String fechaIso = getText(registro, "DES_FECHA");     // 2024-01-01T00:00:00-06:00
            String valorStr = getText(registro, "NUM_VALOR");     // 526.88000000

            if (fechaIso == null || valorStr == null) {
                continue;
            }

            // Convertir fecha ISO a dd/MM/yyyy (sin meter dependencias raras)
            String fecha = convertirFecha(fechaIso);

            BigDecimal valor = new BigDecimal(valorStr);

            // Tu modelo: IndicadorEconomico(fecha, valor, codigoIndicador, null)
            lista.add(new IndicadorEconomico(fecha, valor, codigoIndicador, null));
        }

        return lista;
    }

    private static String getText(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes == null || nodes.getLength() == 0) return null;
        return nodes.item(0).getTextContent().trim();
    }

    /**
     * Convierte "2024-01-01T00:00:00-06:00" a "01/01/2024".
     * Si el formato cambia, devuelve el string original.
     */
    private static String convertirFecha(String fechaIso) {
        try {
            // Tomamos solo YYYY-MM-DD
            String ymd = fechaIso.substring(0, 10); // "2024-01-01"
            String yyyy = ymd.substring(0, 4);
            String mm = ymd.substring(5, 7);
            String dd = ymd.substring(8, 10);
            return dd + "/" + mm + "/" + yyyy;
        } catch (Exception e) {
            return fechaIso;
        }
    }
}
