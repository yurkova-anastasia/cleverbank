package by.yurkova.cleverbank.exception;

/**
 * An exception that indicates a bad or invalid request.
 * This exception is typically thrown when a client sends a request that cannot be processed due to issues
 * such as missing or invalid parameters.
 *
 * @author Yurkova Anastacia
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
