package cl.angel.demo.utils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 *
 * @author angelexperti
 */
public class FechaUtils {

    public static boolean esLaboral(final LocalDate fecha) {
        boolean laboral = false;
        if (fecha != null) {
            DayOfWeek dow = fecha.getDayOfWeek();
            laboral = (!DayOfWeek.SATURDAY.equals(dow) && !DayOfWeek.SUNDAY.equals(dow));
        }
        return laboral;
    }

    public static List<LocalDate> fechasTrabajables(final LocalDate inicio, final LocalDate termino) {
        List<LocalDate> lista = new ArrayList<>();
        if (inicio != null && termino != null) {
            if (Objects.equals(inicio, termino)) {
                if (esLaboral(termino)) {
                    lista.add(termino);
                }
            } else {
                LocalDate actual = inicio;
                while (actual.isBefore(termino)) {
                    if (esLaboral(actual)) {
                        lista.add(actual);
                    }
                    actual = actual.plusDays(1);
                }
            }
        }
        return lista;
    }
}
