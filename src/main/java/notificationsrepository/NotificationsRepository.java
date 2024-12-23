package notificationsrepository;

import entity.Notification;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для работы с уведомлениями пользователей.
 * Предоставляет методы для добавления, получения и обновления статуса уведомлений.
 */
public interface NotificationsRepository {

    /**
     * Добавляет новое уведомление в репозиторий.
     *
     * @param notification объект уведомления, который нужно добавить.
     * @throws NotificationsRepositoryException если произошла ошибка при добавлении уведомления.
     */
    void addNotification(Notification notification) throws NotificationsRepositoryException;

    /**
     * Получает все непрочитанные уведомления для заданного пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список уведомлений, которые не были прочитаны.
     * @throws NotificationsRepositoryException если произошла ошибка при извлечении уведомлений.
     */
    List<Notification> getUnreadNotificationsForUser(UUID userId) throws NotificationsRepositoryException;

    /**
     * Помечает уведомление как прочитанное.
     *
     * @param notificationId идентификатор уведомления, которое нужно пометить как прочитанное.
     * @throws NotificationsRepositoryException если произошла ошибка при обновлении статуса уведомления.
     */
    void markNotificationAsRead(UUID notificationId) throws NotificationsRepositoryException;
}
