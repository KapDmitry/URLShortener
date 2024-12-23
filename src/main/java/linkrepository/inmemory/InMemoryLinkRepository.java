package linkrepository.inmemory;

import linkrepository.LinkRepository;
import linkrepository.LinkRepositoryException;
import entity.Link;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Реализация репозитория ссылок в памяти.
 * <p>Этот репозиторий использует HashMap для хранения ссылок в памяти.</p>
 */
public class InMemoryLinkRepository implements LinkRepository {

    /**
     * Хеш-таблица для хранения ссылок.
     */
    private final Map<UUID, Link> linkStorage = new HashMap<>();

    @Override
    public void saveLink(Link link) throws LinkRepositoryException {
        try {
            if (linkStorage.containsKey(link.getId())) {
                throw new LinkRepositoryException("Ссылка с таким ID уже существует.");
            }
            linkStorage.put(link.getId(), link);
        } catch (Exception e) {
            throw new LinkRepositoryException("Ошибка при сохранении ссылки.", e);
        }
    }

    @Override
    public List<Link> getAll() throws LinkRepositoryException {
        try {
            return new ArrayList<>(linkStorage.values());
        } catch (Exception e) {
            throw new LinkRepositoryException("Ошибка при извлечении ссылок пользователя.", e);
        }
    }

    @Override
    public Optional<Link> getLinkByShortUrl(String shortURL) throws LinkRepositoryException {
        try {
            for (Link link : linkStorage.values()) {
                if (link.getShortURL().equals(shortURL)) {
                    return Optional.of(link);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new LinkRepositoryException("Ошибка при извлечении ссылки по короткой.", e);
        }
    }

    @Override
    public void deleteLink(UUID linkId) throws LinkRepositoryException {
        try {
            if (!linkStorage.containsKey(linkId)) {
                throw new LinkRepositoryException("Ссылка с таким ID не найдена.");
            }
            linkStorage.remove(linkId);
        } catch (Exception e) {
            throw new LinkRepositoryException("Ошибка при удалении ссылки.", e);
        }
    }

    @Override
    public void updateLink(Link link) throws LinkRepositoryException {
        try {
            if (!linkStorage.containsKey(link.getId())) {
                throw new LinkRepositoryException("Ссылка с таким ID не найдена.");
            }
            linkStorage.put(link.getId(), link);
        } catch (Exception e) {
            throw new LinkRepositoryException("Ошибка при обновлении ссылки.", e);
        }
    }
}
