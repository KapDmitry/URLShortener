package entity;

import java.util.UUID;

/**
 * Класс, представляющий уведомление для пользователя.
 * Уведомление содержит информацию о пользователе, сообщение, статус прочтения и уникальный идентификатор.
 */
public class Notification {
    /**
     * Идентификатор нотификации
     */
    private UUID id;

    /**
     * Идентификатор пользователя, которому отправляется уведомление.
     */
    private UUID userID;

    /**
     * Текст уведомления.
     */
    private String message;

    /**
     * Статус прочтения уведомления.
     */
    private boolean isRead;

    /**
     * Конструктор для создания уведомления.
     *
     * @param id Идентификатор нотификации.
     * @param userID Идентификатор пользователя, которому отправляется уведомление.
     * @param message Текст уведомления.
     * @param isRead Статус прочтения уведомления.
     */
    public Notification(UUID id, UUID userID, String message, boolean isRead) {
        this.id = id;
        this.userID = userID;
        this.message = message;
        this.isRead = isRead;
    }

    /**
     * Возвращает уникальный идентификатор уведомления.
     *
     * @return Идентификатор уведомления.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор уведомления.
     *
     * @param id Уникальный идентификатор уведомления.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор пользователя, которому отправлено уведомление.
     *
     * @return Идентификатор пользователя.
     */
    public UUID getUserID() {
        return userID;
    }

    /**
     * Устанавливает идентификатор пользователя, которому отправлено уведомление.
     *
     * @param userID Идентификатор пользователя.
     */
    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    /**
     * Возвращает текст уведомления.
     *
     * @return Сообщение уведомления.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает текст уведомления.
     *
     * @param message Сообщение уведомления.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Возвращает статус прочтения уведомления.
     *
     * @return true, если уведомление прочитано, false в противном случае.
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Устанавливает статус прочтения уведомления.
     *
     * @param isRead true, если уведомление прочитано, false в противном случае.
     */
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * Отмечает уведомление как прочитанное.
     */
    public void markAsRead() {
        this.isRead = true;
    }

    /**
     * Отмечает уведомление как непрочитанное.
     */
    public void markAsUnread() {
        this.isRead = false;
    }

}
