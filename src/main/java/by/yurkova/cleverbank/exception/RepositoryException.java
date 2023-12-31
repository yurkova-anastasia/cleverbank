package by.yurkova.cleverbank.exception;

/**
 * An exception that represents an error related to a repository or data access operation.
 * This exception is typically thrown when there are issues with database interactions or data storage.
 *
 * @author Yurkova Anastacia
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String message) {
        super(message);
    }
}
