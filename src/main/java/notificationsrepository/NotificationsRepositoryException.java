package notificationsrepository;

/**
 * Исключение, которое возникает при ошибках, связанных с репозиторием уведомлений.
 * Это исключение используется для обработки ошибок, которые могут возникнуть при работе
 * с данными уведомлений, например, при их добавлении, удалении или извлечении из репозитория.
 */
public class NotificationsRepositoryException extends Exception {

    /**
     * Конструктор исключения с сообщением об ошибке.
     *
     * @param message Сообщение, которое будет передано в исключение.
     */
    public NotificationsRepositoryException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения с сообщением об ошибке и причиной (исключением).
     *
     * @param message Сообщение, которое будет передано в исключение.
     * @param cause   Причина (другое исключение), которая вызвала это исключение.
     */
    public NotificationsRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

