package cr.ac.ulatinabccr.modelo;

/**
 * DTO: Parámetros necesarios para consultar el servicio del BCCR.
 * La GUI construye estos datos y el backend los utiliza para la consulta.
 */
public class ParametrosConsulta {

    private final int indicador;       // ej: 317 compra, 318 venta
    private final String fechaInicio;   // dd/mm/yyyy
    private final String fechaFinal;    // dd/mm/yyyy
    private final String subNiveles;    // "S" o "N"

    public ParametrosConsulta(int indicador, String fechaInicio, String fechaFinal, String subNiveles) {
        this.indicador = indicador;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.subNiveles = subNiveles;
    }

    public int getIndicador() {
        return indicador;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public String getSubNiveles() {
        return subNiveles;
    }
}
