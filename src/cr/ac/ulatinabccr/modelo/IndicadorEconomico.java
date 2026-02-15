package cr.ac.ulatinabccr.modelo;

import java.math.BigDecimal;

/**
 * DTO: representa un dato devuelto por el BCCR para un indicador en una fecha.
 */
public class IndicadorEconomico {

    private final String fecha;              // dd/mm/yyyy (mantener simple para el curso)
    private final BigDecimal valor;          // valor numérico del indicador
    private final int codigoIndicador;       // ej: 317 o 318
    private final String nombreIndicador;    // opcional (puede ser null)

    public IndicadorEconomico(String fecha, BigDecimal valor, int codigoIndicador, String nombreIndicador) {
        this.fecha = fecha;
        this.valor = valor;
        this.codigoIndicador = codigoIndicador;
        this.nombreIndicador = nombreIndicador;
    }

    public String getFecha() {
        return fecha;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public int getCodigoIndicador() {
        return codigoIndicador;
    }

    public String getNombreIndicador() {
        return nombreIndicador;
    }

  @Override
public String toString() {
    java.text.NumberFormat formato =
            java.text.NumberFormat.getNumberInstance();
    formato.setMinimumFractionDigits(2);
    formato.setMaximumFractionDigits(2);

    return fecha + " -> " + formato.format(valor);
}

}
