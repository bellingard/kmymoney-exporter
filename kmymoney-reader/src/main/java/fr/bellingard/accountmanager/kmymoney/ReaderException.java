package fr.bellingard.accountmanager.kmymoney;

/**
 * Exception for the KMY reader
 */
public class ReaderException extends Exception {

    /**
     * Creates the exception.
     *
     * @param message
     * @param cause
     */
    public ReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
