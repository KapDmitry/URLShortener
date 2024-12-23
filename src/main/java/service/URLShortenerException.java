package service;

/**
 * Исключение, которое выбрасывается, когда возникала ошибка работы сервиса.
 */
public class URLShortenerException extends Exception {

    /**
     * Конструктор, который создает исключение с сообщением.
     *
     * @param message сообщение об ошибке.
     */
    public URLShortenerException(String message) {
        super(message);
    }

    /**
     * Конструктор, который создает исключение с сообщением.
     *
     * @param message сообщение об ошибке.
     * @param cause оборачиваемое исключение.
     */
    public URLShortenerException(String message, Throwable cause) {
        super(message, cause);
    }

}
