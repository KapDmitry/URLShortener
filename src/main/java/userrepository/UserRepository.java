package userrepository;

import entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс UserRepository предоставляет операции для управления пользователями.
 *
 * <p>Определяет методы для добавления пользователей и проверки их существования.</p>
 */
public interface UserRepository {

    /**
     * Добавляет пользователя в репозиторий.
     *
     * @param user объект пользователя, который необходимо добавить.
     * @throws UserRepositoryException если произошла ошибка при добавлении пользователя.
     */
    void addUser(User user) throws UserRepositoryException;

    /**
     * Отдает пользователя по его UUID
     *
     * @param userId уникальный идентификатор пользователя.
     * @return true, если пользователь существует; false в противном случае.
     * @throws UserRepositoryException если произошла ошибка при проверке существования пользователя.
     */
    Optional<User> getUser(UUID userId) throws UserRepositoryException;
}


