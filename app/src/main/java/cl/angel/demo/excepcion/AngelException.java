package cl.angel.demo.excepcion;

/**
 *
 * @author angelexperti
 */
public class AngelException extends RuntimeException {

    public AngelException() {
        super("Error no esperado");
    }

    public AngelException(String message) {
        super(message);
    }
}
