package cr.ac.ulatinabccr.infraestructura.soap;

import cr.ac.ulatinabccr.modelo.ParametrosConsulta;

/**
 * Mock para desarrollo y pruebas sin depender del servicio real.
 * Retorna XML simple que luego será parseado por el parser.
 */
public class BccrSoapClientMock implements BccrSoapClient {

    @Override
    public String obtenerIndicadoresEconomicosXML(ParametrosConsulta params) {
        int ind = params.getIndicador();

        // XML de ejemplo: estructura simple intencional para desarrollo inicial.
        return """
        <Datos>
          <Registro>
            <Fecha>01/01/2024</Fecha>
            <Valor>520.34</Valor>
            <Indicador>%d</Indicador>
          </Registro>
          <Registro>
            <Fecha>02/01/2024</Fecha>
            <Valor>521.10</Valor>
            <Indicador>%d</Indicador>
          </Registro>
        </Datos>
        """.formatted(ind, ind);
    }
}
