package userrepository;

/**
 * Исключение, которое выбрасывается при ошибках в репозитории пользователей.
 */
public class UserRepositoryException extends Exception {

    /**
     * Конструктор, который создает исключение с сообщением.
     *
     * @param message сообщение об ошибке.
     */
    public UserRepositoryException(String message) {
        super(message);
    }

}
