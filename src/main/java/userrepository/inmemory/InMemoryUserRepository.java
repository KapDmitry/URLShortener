package userrepository.inmemory;

import entity.User;
import userrepository.UserRepository;
import userrepository.UserRepositoryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация интерфейса UserRepository для хранения пользователей в памяти.
 *
 * <p>Использует карту (HashMap) для хранения пользователей, где ключом является уникальный
 * идентификатор пользователя (UUID), а значением - объект пользователя.</p>
 */
public class InMemoryUserRepository implements UserRepository {

    /**
     * Хранилище пользователей в памяти.
     * Ключ - уникальный идентификатор пользователя (UUID), значение - объект пользователя (User).
     */
    private final Map<UUID, User> userStorage = new HashMap<>();

    /**
     * Добавляет пользователя в хранилище.
     *
     * @param user объект пользователя, который необходимо добавить.
     */
    @Override
    public void addUser(User user) throws UserRepositoryException {
        if (user == null || user.getId() == null) {
            throw new UserRepositoryException("User or User ID cannot be null");
        }
        userStorage.put(user.getId(), user);
    }

    /**
     * Проверяет, существует ли пользователь в хранилище.
     *
     * @param userId уникальный идентификатор пользователя.
     * @return true, если пользователь существует; false в противном случае.
     */
    @Override
    public Optional<User> getUser(UUID userId) throws UserRepositoryException {
        if (userId == null) {
            throw new UserRepositoryException("User ID cannot be null");
        }
        if (!userStorage.containsKey(userId)) {
            return Optional.empty();
        }
        return Optional.of(userStorage.get(userId));
    }
}

