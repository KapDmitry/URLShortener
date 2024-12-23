package linkrepository;

import entity.Link;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

/**
 * Интерфейс LinkRepository предоставляет операции для работы с сокращенными ссылками.
 *
 * <p>Методы репозитория позволяют добавлять, извлекать, обновлять и удалять сокращенные ссылки,
 * а также управлять счетчиком посещений.</p>
 */
public interface LinkRepository {

    /**
     * Сохраняет ссылку в репозитории.
     *
     * @param link объект сокращенной ссылки, который необходимо сохранить.
     * @throws LinkRepositoryException если произошла ошибка при сохранении ссылки.
     */
    void saveLink(Link link) throws LinkRepositoryException;

    /**
     * Возвращает все ссылки.
     *
     * @return список ссылок или пустой список, если ссылки отсутствуют.
     * @throws LinkRepositoryException если произошла ошибка при извлечении ссылок.
     */
    List<Link> getAll() throws LinkRepositoryException;

    /**
     * Возвращает ссылку по короткой ссылке.
     *
     * @param shortURL сокращенная ссылка.
     * @return {@link Optional} содержащий объект {@link Link}, если ссылка найдена; пустой {@link Optional} в противном случае.
     * @throws LinkRepositoryException если произошла ошибка при извлечении ссылки.
     */
    Optional<Link> getLinkByShortUrl(String shortURL) throws LinkRepositoryException;

    /**
     * Удаляет ссылку по её идентификатору.
     *
     * @param linkId уникальный идентификатор ссылки.
     * @throws LinkRepositoryException если произошла ошибка при удалении ссылки.
     */
    void deleteLink(UUID linkId) throws LinkRepositoryException;

    /**
     * Обновляет ссылку.
     *
     * @param link обновленная ссылка.
     * @throws LinkRepositoryException если произошла ошибка при удалении ссылки.
     */
    void updateLink(Link link) throws LinkRepositoryException;
}
