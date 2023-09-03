package by.yurkova.cleverbank.exception;

/**
 * An exception that indicates an error during check generation.
 * This exception is thrown when there are issues with generating checks or related processes.
 *
 * @author Yurkova Anastacia
 */
public class CheckGenerationException extends RuntimeException {

    public CheckGenerationException(String message) {
        super(message);
    }
}
