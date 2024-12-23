package linkrepository;

/**
 * Исключение, которое выбрасывается при ошибках в репозитории ссылок.
 */
public class LinkRepositoryException extends Exception {

    /**
     * Конструктор с сообщением.
     *
     * @param message сообщение об ошибке.
     */
    public LinkRepositoryException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message сообщение об ошибке.
     * @param cause   причина ошибки.
     */
    public LinkRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

