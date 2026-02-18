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

/**
 * Parser XML basado en DOM (Document Object Model).
 *
 * Responsabilidad:
 * - Recibe el XML crudo (String) devuelto por el BCCR.
 * - Construye un Document en memoria.
 * - Extrae los nodos relevantes y los convierte a objetos IndicadorEconomico.
 *
 * Importante:
 * Esta clase NO llama al BCCR (eso lo hace la infraestructura SOAP).
 * Solo interpreta el XML y lo transforma a datos utilizables por negocio/GUI.
 *
 * Se usa DOM por ser más didáctico para el curso:
 * permite navegar el XML como un árbol y extraer nodos por nombre.
 */
public class IndicadoresXmlParserDomXPath implements IndicadoresXmlParser {

    /**
     * Parsea el XML del BCCR para un indicador específico.
     *
     * @param xml XML crudo (String) devuelto por ObtenerIndicadoresEconomicosXML.
     * @param codigoIndicador código del indicador consultado (ej: 317 compra, 318 venta).
     * @return lista de IndicadorEconomico (una fila por fecha dentro del rango).
     * @throws Exception si el XML está mal formado o si ocurre un error al parsear.
     */
    @Override
    public List<IndicadorEconomico> parse(String xml, int codigoIndicador) throws Exception {
        List<IndicadorEconomico> lista = new ArrayList<>();

        // Validación básica: si no hay XML, no hay nada que procesar.
        if (xml == null || xml.isBlank()) {
            return lista;
        }

        // DOM: construye un árbol del XML completo en memoria.
        // Nota: namespaceAware = false porque el XML que devuelve este método del BCCR
        // llega sin prefijos/namespace en la respuesta que estamos procesando.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);

        // Parsear el String XML a Document
        Document doc = dbf.newDocumentBuilder().parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))
        );

        /*
         * El XML real del BCCR para este servicio tiene una estructura como:
         *
         * <Datos_de_INGC011_CAT_INDICADORECONOMIC>
         *   <INGC011_CAT_INDICADORECONOMIC>
         *     <COD_INDICADORINTERNO>318</COD_INDICADORINTERNO>
         *     <DES_FECHA>2024-01-01T00:00:00-06:00</DES_FECHA>
         *     <NUM_VALOR>526.88000000</NUM_VALOR>
         *   </INGC011_CAT_INDICADORECONOMIC>
         *   ...
         * </Datos_de_INGC011_CAT_INDICADORECONOMIC>
         *
         * Por eso buscamos las "filas" con getElementsByTagName("INGC011_CAT_INDICADORECONOMIC").
         */
        NodeList registros = doc.getElementsByTagName("INGC011_CAT_INDICADORECONOMIC");

        // Recorrer cada fila del XML y mapearla a un DTO IndicadorEconomico
        for (int i = 0; i < registros.getLength(); i++) {
            Element registro = (Element) registros.item(i);

            // Extraemos los valores de interés
            String fechaIso = getText(registro, "DES_FECHA"); // formato ISO con zona horaria
            String valorStr = getText(registro, "NUM_VALOR"); // número con muchos decimales

            // Si falta algún campo, ignoramos ese registro para evitar nulls
            if (fechaIso == null || valorStr == null) {
                continue;
            }

            // Convertir fecha ISO (YYYY-MM-DD...) a dd/MM/yyyy (formato del curso/GUI)
            String fecha = convertirFecha(fechaIso);

            // NUM_VALOR viene como texto, se convierte a BigDecimal para precisión (dinero/tipo de cambio)
            BigDecimal valor = new BigDecimal(valorStr);

            // nombreIndicador se deja null por ahora (opcional).
            lista.add(new IndicadorEconomico(fecha, valor, codigoIndicador, null));
        }

        return lista;
    }

    /**
     * Obtiene el texto del primer nodo hijo con el tag indicado.
     * Se usa para evitar repetir lógica de NodeList en cada campo.
     */
    private static String getText(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes == null || nodes.getLength() == 0) return null;
        return nodes.item(0).getTextContent().trim();
    }

    /**
     * Convierte la fecha que entrega el BCCR en ISO:
     * "2024-01-01T00:00:00-06:00"
     * a un formato más simple para presentar:
     * "01/01/2024"
     *
     * Si el formato cambia, se devuelve el string original para no romper la ejecución.
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
            // Fallback: si ocurre un error, se retorna la fecha original.
            return fechaIso;
        }
    }
}
