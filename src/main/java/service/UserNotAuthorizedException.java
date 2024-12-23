package service;

/**
 * Исключение, которое выбрасывается, когда пользователь не авторизован.
 */
public class UserNotAuthorizedException extends Exception {

    /**
     * Конструктор, который создает исключение со стандартным сообщением.
     *
     */
    public UserNotAuthorizedException() {
        super("Пользователь не авторизован");
    }

}

