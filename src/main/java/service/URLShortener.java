package service;

import entity.Link;
import entity.Notification;
import entity.User;
import linkrepository.LinkRepository;
import linkrepository.LinkRepositoryException;
import notificationsrepository.NotificationsRepository;
import notificationsrepository.NotificationsRepositoryException;
import urlgenerator.URLGenerator;
import userrepository.UserRepository;
import userrepository.UserRepositoryException;

import java.awt.*;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.Duration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс, реализующий логику работы с сервисом сокращения ссылок.
 * Он обеспечивает функционал для логина пользователя, регистрации нового пользователя,
 * создания новых сокращенных ссылок и работы с репозиториями.
 */
public class URLShortener {
    /**
     * Репозиторий ссылок.
     */
    private LinkRepository linkRepository;

    /**
     * Репозиторий пользователей.
     */
    private UserRepository userRepository;

    /**
     * Репозиторий нотификаций.
     */
    private NotificationsRepository notificationsRepository;

    /**
     * Текущий пользователь.
     */
    private User currentUser;

    /**
     * Конфиг приложения
     */
    private URLShortenerConfig config;

    /**
     * Генератор коротких ссылок.
     */
    private URLGenerator generator;

    /**
     * Конструктор класса URLShortener.
     * Инициализирует все зависимости, включая репозитории и конфигурацию.
     *
     * @param linkRepository Репозиторий для работы с короткими ссылками
     * @param userRepository Репозиторий для работы с пользователями
     * @param notificationsRepository Репозиторий для работы с уведомлениями
     * @param generator Сервис для генерации коротких ссылок
     * @param config Конфигурация для сервиса сокращения ссылок
     */
    public URLShortener(LinkRepository linkRepository, UserRepository userRepository, NotificationsRepository notificationsRepository , URLGenerator generator, URLShortenerConfig config) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.notificationsRepository = notificationsRepository;
        this.generator = generator;
        this.config = config;
    }

    /**
     * Логин пользователя в систему.
     * Проверяет наличие пользователя по заданному UUID и устанавливает его как текущего пользователя.
     *
     * @param userId UUID пользователя
     * @throws URLShortenerException Если пользователь не найден или произошла ошибка при проверке
     */
    public void login(UUID userId) throws URLShortenerException {
        try {
            Optional<User> user = userRepository.getUser(userId);

            if (user.isEmpty()) {
                throw new URLShortenerException("User not found");
            }

            currentUser = user.get();
        }
        catch (UserRepositoryException e) {
            throw new URLShortenerException("Ошибка при проверке существования пользователя: " + e.getMessage(),e);
        }
    }

    /**
     * Регистрация нового пользователя.
     * Добавляет нового пользователя в репозиторий и устанавливает его как текущего.
     *
     * @param user Объект пользователя, которого необходимо зарегистрировать
     * @return UUID нового пользователя
     * @throws URLShortenerException Если произошла ошибка при добавлении пользователя
     */
    public UUID register(User user) throws URLShortenerException {
        try {
            userRepository.addUser(user);
            currentUser = user;
            return user.getId();
        }
        catch (UserRepositoryException e) {
            throw new URLShortenerException("Ошибка при добавлении пользователя: "+e.getMessage(),e);
        }
    }

    /**
     * Создание новой сокращенной ссылки.
     * Генерирует уникальную короткую ссылку для заданной длинной ссылки, устанавливает время жизни и количество кликов.
     *
     * @param longLink Длинная ссылка, для которой создается сокращенная версия
     * @param timeToLive Время жизни ссылки в формате Duration
     * @param numOfClicks Максимальное количество кликов по ссылке
     * @return Сокращенная ссылка в виде объекта Link
     * @throws UserNotAuthorizedException Если пользователь не авторизован
     * @throws URLShortenerException Если не удалось создать ссылку или произошла ошибка при взаимодействии с репозиториями
     */
    public Link createLink(String longLink, Duration timeToLive, int numOfClicks) throws UserNotAuthorizedException, URLShortenerException {
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }

        if (timeToLive.compareTo(config.getMaxLinkTTL()) > 0) {
            throw new URLShortenerException("Время жизни ссылки не может превышать максимальное значение: " + config.getMaxLinkTTL().toSeconds() + " c.");
        }

        if (numOfClicks < 0) {
            throw new URLShortenerException("Число кликов не может быть < 0");
        }

        if (timeToLive.toSeconds() == 0) {
            timeToLive = config.getMaxLinkTTL();
        }

        if (numOfClicks  == 0) {
            numOfClicks = config.getMaxLinkClicks();
        }


        int retries = 3;
        String shortLink = null;
        Link link = null;

        while (retries > 0) {
            shortLink = generator.generateShortLink();

            try {
                Optional<Link> existingLink = linkRepository.getLinkByShortUrl(shortLink);

                if (existingLink.isPresent()) {
                    retries--;
                    if (retries == 0) {
                        throw new URLShortenerException("Не удалось сгенерировать уникальную короткую ссылку после нескольких попыток.");
                    }
                    continue;
                }

                LocalDateTime now = LocalDateTime.now();
                link = new Link(
                        UUID.randomUUID(),
                        longLink,
                        shortLink,
                        currentUser.getId(),
                        numOfClicks,
                        now.plus(timeToLive),
                        now
                );

                linkRepository.saveLink(link);
                return link;
            }
            catch (LinkRepositoryException e) {
                throw new URLShortenerException("Ошибка при добавлении ссылки: " + e.getMessage(),e);
            }
        }

        throw new URLShortenerException("Не удалось создать ссылку после нескольких попыток.");
    }

    /**
     * Обновляет время жизни ссылки.
     *
     * @param shortLink    короткая ссылка, для которой необходимо обновить время жизни.
     * @param newTimeToLive новое время жизни в виде объекта Duration.
     *                      Время жизни добавляется к дате создания ссылки (createDt).
     * @return обновленный объект {@link Link}, содержащий все параметры ссылки после обновления.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     * @throws URLShortenerException если:
     *         <ul>
     *             <li>ссылка не найдена,</li>
     *             <li>пользователь не является владельцем ссылки,</li>
     *             <li>время жизни превышает максимально допустимое значение из конфигурации,</li>
     *             <li>или произошла ошибка при сохранении изменений в репозитории.</li>
     *         </ul>
     */
    public Link updateLinkExpiration(String shortLink, Duration newTimeToLive) throws UserNotAuthorizedException, URLShortenerException {
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }

        if (newTimeToLive.compareTo(config.getMaxLinkTTL()) > 0) {
            throw new URLShortenerException("Время жизни ссылки превышает максимально допустимое значение: " + config.getMaxLinkTTL().toSeconds() + " c.");
        }

        try {
            Optional<Link> linkOptional = linkRepository.getLinkByShortUrl(shortLink);
            if (linkOptional.isEmpty()) {
                throw new URLShortenerException("Ссылка не найдена.");
            }

            Link link = linkOptional.get();

            if (!link.getUserID().equals(currentUser.getId())) {
                throw new URLShortenerException("Вы не являетесь владельцем этой ссылки.");
            }

            if (link.getExpireDt().isBefore(LocalDateTime.now())) {
                throw new URLShortenerException("Время жизни ссылки истекло.");
            }

            link.setExpireDt(link.getCreateDt().plus(newTimeToLive));
            linkRepository.updateLink(link);
            return link;
        } catch (LinkRepositoryException e) {
            throw new URLShortenerException("Ошибка при обновлении времени жизни ссылки: " + e.getMessage(),e);
        }
    }


    /**
     * Обновляет максимальное количество кликов для ссылки.
     *
     * @param shortLink       короткая ссылка, для которой необходимо обновить количество кликов.
     * @param newMaxClicks    новое максимальное количество кликов.
     * @return обновленный объект {@link Link}, содержащий все параметры ссылки после обновления.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     * @throws URLShortenerException если:
     *         <ul>
     *             <li>ссылка не найдена,</li>
     *             <li>пользователь не является владельцем ссылки,</li>
     *             <li>новое количество кликов превышает максимально допустимое значение из конфигурации,</li>
     *             <li>или произошла ошибка при сохранении изменений в репозитории.</li>
     *         </ul>
     */
    public Link updateLinkMaxClicks(String shortLink, int newMaxClicks) throws UserNotAuthorizedException, URLShortenerException {
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }

        if (newMaxClicks < 0) {
            throw new URLShortenerException("Число кликов не может быть < 0");
        }

        try {
            Optional<Link> linkOptional = linkRepository.getLinkByShortUrl(shortLink);
            if (linkOptional.isEmpty()) {
                throw new URLShortenerException("Ссылка не найдена.");
            }

            Link link = linkOptional.get();

            if (!link.getUserID().equals(currentUser.getId())) {
                throw new URLShortenerException("Вы не являетесь владельцем этой ссылки.");
            }

            if (link.getExpireDt().isBefore(LocalDateTime.now())) {
                throw new URLShortenerException("Время жизни ссылки истекло.");
            }

            link.setClickCount(newMaxClicks);
            linkRepository.updateLink(link);
            return link;
        } catch (LinkRepositoryException e) {
            throw new URLShortenerException("Ошибка при обновлении количества кликов для ссылки: " + e.getMessage(), e);
        }
    }



    /**
     * Выполняет запрос по длинной ссылке, связанной с указанной короткой ссылкой.
     * Открывает длинную ссылку в браузере.
     *
     * @param shortLink короткая ссылка, по которой необходимо выполнить запрос.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     * @throws URLShortenerException если:
     *         <ul>
     *             <li>ссылка не найдена,</li>
     *             <li>пользователь не является владельцем ссылки,</li>
     *             <li>ссылка устарела (время жизни истекло),</li>
     *             <li>число доступных кликов = 0,</li>
     *         </ul>
     */
    public void fetchShortLink(String shortLink) throws UserNotAuthorizedException, URLShortenerException {
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }

        try {
            Optional<Link> linkOptional = linkRepository.getLinkByShortUrl(shortLink);
            if (linkOptional.isEmpty()) {
                throw new URLShortenerException("Ссылка не найдена.");
            }

            Link link = linkOptional.get();

            if (!link.getUserID().equals(currentUser.getId())) {
                throw new URLShortenerException("Вы не являетесь владельцем этой ссылки.");
            }

            if (link.getExpireDt().isBefore(LocalDateTime.now())) {
                throw new URLShortenerException("Время жизни ссылки истекло.");
            }

            if (link.getClickCount() <= 0) {
                throw new URLShortenerException("Число доступных кликов для этой ссылки равно 0.");
            }

            String longLink = link.getLongURL();

            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    URI uri = new URI(longLink);
                    desktop.browse(uri);
                } else {
                    throw new URLShortenerException("Открытие браузера не поддерживается на этой системе.");
                }

                link.setClickCount(link.getClickCount() - 1);
                linkRepository.updateLink(link);

            }
            catch (URISyntaxException e) {
                throw new URLShortenerException("Ошибка при чтении ссылки: "+ e.getMessage(), e);
            }
            catch (IOException e) {
                throw new URLShortenerException("Ошибка при открытии ссылки в браузере: " + e.getMessage(), e);
            }

        }
        catch (LinkRepositoryException e) {
            throw new URLShortenerException("Ошибка при доступе к репозиторию ссылок: " + e.getMessage(), e);
        }
    }



    /**
     * Удаляет ссылку.
     *
     * @param shortLink короткая ссылка, которую нужно удалить.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     * @throws URLShortenerException если:
     *         <ul>
     *             <li>ссылка не найдена,</li>
     *             <li>пользователь не является владельцем ссылки.</li>
     *         </ul>
     */
    public void deleteLink(String shortLink) throws UserNotAuthorizedException, URLShortenerException {
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }

        try {
            Optional<Link> linkOptional = linkRepository.getLinkByShortUrl(shortLink);
            if (linkOptional.isEmpty()) {
                throw new URLShortenerException("Ссылка не найдена.");
            }

            Link link = linkOptional.get();

            if (!link.getUserID().equals(currentUser.getId())) {
                throw new URLShortenerException("Вы не являетесь владельцем этой ссылки.");
            }

            if (link.getExpireDt().isBefore(LocalDateTime.now())) {
                throw new URLShortenerException("Время жизни ссылки истекло.");
            }

            linkRepository.deleteLink(link.getId());

        } catch (LinkRepositoryException e) {
            throw new URLShortenerException("Ошибка при удалении ссылки: " + e.getMessage(),e);
        }
    }

    /**
     * Удаляет все истекшие ссылки или ссылки с превышенным количеством кликов для текущего пользователя
     * и создает уведомления для каждой удаленной ссылки.
     *
     *  @throws URLShortenerException если возникла ошибка в процессе удаления ссылок или создания уведомлений.
     */
    public void deleteExpiredOrExceededLinks() throws URLShortenerException {

        try {
            List<Link> userLinks = linkRepository.getAll();

            for (Link link : userLinks) {
                boolean shouldDelete = false;
                Reason reason = null;

                if (link.getExpireDt() != null && link.getExpireDt().isBefore(LocalDateTime.now())) {
                    shouldDelete = true;
                    reason = Reason.EXPIRED;
                } else if (link.getClickCount() == 0) {
                    shouldDelete = true;
                    reason = Reason.OUT_OF_CLICKS;
                }

                if (shouldDelete) {
                    linkRepository.deleteLink(link.getId());

                    String message = String.format("Ссылка с коротким адресом %s была удалена по причине: %s.",
                            link.getShortURL(), reason.getDescription());

                    Notification notification = new Notification(UUID.randomUUID(),link.getUserID(), message, false);

                    notificationsRepository.addNotification(notification);
                }
            }
        } catch (LinkRepositoryException e) {
            throw new URLShortenerException("Ошибка при удалении ссылок пользователя: " + e.getMessage(), e);
        } catch (NotificationsRepositoryException e) {
            throw new URLShortenerException("Ошибка при добавлении уведомлений для пользователя: " + e.getMessage(), e);
        }
    }

    /**
     * Получает все уведомления для текущего пользователя и помечает их как прочитанные.
     *
     * @return Список уведомлений для текущего пользователя.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     * @throws URLShortenerException если ошибка при получении или обновлении уведомлений.
     */
    public List<Notification> getAllUnreadNotificationsForUser() throws UserNotAuthorizedException, URLShortenerException {
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }

        try {
            List<Notification> unreadNotifications = notificationsRepository.getUnreadNotificationsForUser(currentUser.getId());

            for (Notification notification : unreadNotifications) {
                notification.markAsRead();
                notificationsRepository.markNotificationAsRead(notification.getId());
            }

            return unreadNotifications;

        } catch (NotificationsRepositoryException e) {
            throw new URLShortenerException("Ошибка при получении или обновлении уведомлений: " + e.getMessage(),e);
        }
    }



    /**
     * Возвращает максимальное количество кликов для ссылки по умолчанию.
     * Этот метод извлекает значение из конфигурации сервиса сокращения ссылок.
     *
     * @return Максимальное количество кликов для ссылки
     */
    public int getDefaultClicks() {
        return config.getMaxLinkClicks();
    }

    /**
     * Возвращает максимальное время жизни ссылки по умолчанию.
     * Этот метод извлекает значение из конфигурации сервиса сокращения ссылок.
     *
     * @return Максимальное время жизни ссылки в формате Duration
     */
    public Duration getDefaultTTL() {
        return config.getMaxLinkTTL();
    }

    /**
     * Возвращает все короткие ссылки текущего пользователя.
     * @return ссылки текушего пользователя.
     */
    public List<Link> getAllLinksForCurrentUser() throws UserNotAuthorizedException, URLShortenerException {
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }

        try {
            List<Link> ans = new ArrayList<>();
            List<Link> links = linkRepository.getAll();

            for (Link link : links) {
                if (link.getUserID().equals(currentUser.getId())) {
                    ans.add(link);
                }
            }

            return ans;
        }
        catch (LinkRepositoryException e) {
            throw new URLShortenerException("Ошибка при получении ссылок пользователя", e);
        }
    }
}
