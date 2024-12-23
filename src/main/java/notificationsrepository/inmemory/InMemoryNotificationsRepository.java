package notificationsrepository.inmemory;

import notificationsrepository.NotificationsRepository;
import entity.Notification;
import notificationsrepository.NotificationsRepositoryException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализация репозитория уведомлений в памяти.
 * Этот класс использует HashMap для хранения уведомлений и их статусов.
 */
public class InMemoryNotificationsRepository implements NotificationsRepository {

    /**
     * Хеш-таблица для хранения нотификаций.
     */
    private final Map<UUID, Notification> notificationsStorage = new HashMap<>();

    /**
     * Добавляет новое уведомление в репозиторий.
     *
     * @param notification объект уведомления, который нужно добавить.
     * @throws NotificationsRepositoryException если произошла ошибка при добавлении уведомления.
     */
    @Override
    public void addNotification(Notification notification) throws NotificationsRepositoryException {
        try {
            if (notification == null) {
                throw new NotificationsRepositoryException("Уведомление не может быть null");
            }
            notificationsStorage.put(notification.getId(), notification);
        } catch (Exception e) {
            throw new NotificationsRepositoryException("Ошибка при добавлении уведомления.", e);
        }
    }

    /**
     * Получает все непрочитанные уведомления для заданного пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список уведомлений, которые не были прочитаны.
     * @throws NotificationsRepositoryException если произошла ошибка при извлечении уведомлений.
     */
    @Override
    public List<Notification> getUnreadNotificationsForUser(UUID userId) throws NotificationsRepositoryException {
        try {
            List<Notification> unreadNotifications = new ArrayList<>();
            for (Notification notification : notificationsStorage.values()) {
                if (notification.getUserID().equals(userId) && !notification.isRead()) {
                    unreadNotifications.add(notification);
                }
            }
            return unreadNotifications;
        } catch (Exception e) {
            throw new NotificationsRepositoryException("Ошибка при извлечении непрочитанных уведомлений.", e);
        }
    }

    /**
     * Помечает уведомление как прочитанное.
     *
     * @param notificationId идентификатор уведомления, которое нужно пометить как прочитанное.
     * @throws NotificationsRepositoryException если произошла ошибка при обновлении статуса уведомления.
     */
    @Override
    public void markNotificationAsRead(UUID notificationId) throws NotificationsRepositoryException {
        try {
            Notification notification = notificationsStorage.get(notificationId);
            if (notification == null) {
                throw new NotificationsRepositoryException("Уведомление с таким ID не найдено.");
            }
            notification.markAsRead();
        } catch (Exception e) {
            throw new NotificationsRepositoryException("Ошибка при пометке уведомления как прочитанного.", e);
        }
    }
}


