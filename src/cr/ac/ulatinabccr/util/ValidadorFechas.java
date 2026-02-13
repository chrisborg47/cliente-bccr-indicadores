package cr.ac.ulatinabccr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Utilidad para validar fechas en formato dd/MM/yyyy.
 * Se usa antes de enviar parámetros al servicio.
 */
public class ValidadorFechas {

    private static final String FORMATO = "dd/MM/yyyy";

    private ValidadorFechas() {
        // Evita instancias (clase utilitaria).
    }

    public static boolean esFormatoValido(String fecha) {
        if (fecha == null) return false;

        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO);
        sdf.setLenient(false); // IMPORTANTÍSIMO: no permite 32/01/2024, etc.

        try {
            sdf.parse(fecha.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
