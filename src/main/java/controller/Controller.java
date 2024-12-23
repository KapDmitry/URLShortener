package controller;

import entity.Notification;
import service.URLShortener;
import entity.Link;
import entity.User;
import service.URLShortenerException;
import service.UserNotAuthorizedException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;
import java.util.Scanner;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для взаимодействия между пользователем и сервисом.
 *
 */
public class Controller {
    /**
     * Сервис коротких ссылок.
     */
    private URLShortener shortener;

    /**
     * Сканер для считывания пользовательского ввода.
     * */
    private Scanner scanner;

    /**
     * Конструктор контроллера.
     *
     * @param shortener сервис для работы с сокращением URL.
     */
    public Controller(URLShortener shortener) {
        this.shortener = shortener;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Метод для запуска основного цикла работы приложения.
     * Он будет вызывать методы для отображения меню и обработки команд.
     */
    public void mainLoop() {
        while (true) {
            showMenu();
            processCommand();
            clearExpiredAndExcessClicksLinks();
            displayUserNotifications();
        }
    }

    /**
     * Метод для отображения главного меню.
     */
    private void showMenu() {
        System.out.println("\n===== URL Shortener =====");
        System.out.println("1. Создать ссылку");
        System.out.println("2. Войти в существующий аккаунт");
        System.out.println("3. Зарегистрироваться");
        System.out.println("4. Изменить время жизни ссылки");
        System.out.println("5. Изменить число кликов для ссылки");
        System.out.println("6. Сделать запрос по короткой ссылке");
        System.out.println("7. Удалить короткую ссылку");
        System.out.println("8. Вывести ссылки пользователя");
        System.out.println("9. Выйти");
        System.out.print("Введите команду: ");
    }

    /**
     * Метод для обработки команды, введенной пользователем.
     */
    private void processCommand() {
        int choice = 0;

        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Неправильный ввод, повторите снова.");
        }
        scanner.nextLine();

        try {
            switch (choice) {
                case 1:
                    createLink();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    register();
                    break;
                case 4:
                    updateLinkExpiration();
                    break;
                case 5:
                    updateLinkMaxClicks();
                    break;
                case 6:
                    requestShortLink();
                    break;
                case 7:
                    deleteLink();
                    break;
                case 8:
                    displayUserLinks();
                    break;
                case 9:
                    System.out.println("Выход...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Некорректная команда.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Метод для очистки ссылок, которые истекли по времени или превышают допустимое количество кликов.
     */
    private void clearExpiredAndExcessClicksLinks() {
        try {
            shortener.deleteExpiredOrExceededLinks();
        } catch (Exception e) {
            System.out.println("Ошибка при очистке ссылок: " + e.getMessage());
        }
    }

    /**
     * Метод для получения и вывода уведомлений для текущего пользователя.
     */
    private void displayUserNotifications() {
        try {
            List<Notification> notifications = shortener.getAllUnreadNotificationsForUser();
            if (notifications.isEmpty()) {
                System.out.println("У вас нет новых уведомлений.");
            } else {
                System.out.println("Новые уведомления:");
                for (Notification notification : notifications) {
                    System.out.println("Сообщение: " + notification.getMessage());
                    System.out.println("--------------------------------");
                }
            }
        } catch (UserNotAuthorizedException e) {
            System.out.println("Чтобы получить уведомления войдите в свой аккаунт");
        } catch (URLShortenerException ee) {
            System.out.println("Ошибка при получении уведомлений: " + ee.getMessage());
        }
    }


    /**
     * Создание новой ссылки.
     */
    private void createLink() {
        try {
            System.out.print("Введите длинную ссылку: ");
            String longLink = scanner.nextLine();
            Duration defaultTTL = shortener.getDefaultTTL();
            System.out.print("Введите время жизни ссылки (например, 30s для 30 секунд, 30m для 30 минут, 30d для 30 дней) (по умолчанию " + defaultTTL.toSeconds() + " секунд): ");
            String timeInput = scanner.nextLine();
            Duration timeToLive = parseDuration(timeInput);
            int defuaultClicks = shortener.getDefaultClicks();
            System.out.print("Введите количество кликов для ссылки (по умолчанию " + defuaultClicks + "): ");
            String clicksInput = scanner.nextLine();
            int numClicks = 0;
            if (!clicksInput.isEmpty()) {
                numClicks = Integer.parseInt(clicksInput);
            }


            try {
                Link newLink = shortener.createLink(longLink, timeToLive, numClicks);
                System.out.println("Ссылка успешно создана!");
                System.out.println("Короткая ссылка: " + newLink.getShortURL());
                System.out.println("Длинная ссылка: " + newLink.getLongURL());
                System.out.println("Дата истечения: " + newLink.getExpireDt());
                System.out.println("Количество кликов: " + newLink.getClickCount());
            } catch (UserNotAuthorizedException e) {
                System.out.println("Вы не авторизованы, создаем нового пользователя.");
                register();
                Link newLink = shortener.createLink(longLink, timeToLive, numClicks);
                System.out.println("Ссылка успешно создана!");
                System.out.println("Короткая ссылка: " + newLink.getShortURL());
                System.out.println("Длинная ссылка: " + newLink.getLongURL());
                System.out.println("Дата истечения: " + newLink.getExpireDt());
                System.out.println("Количество кликов: " + newLink.getClickCount());
            }
        } catch (URLShortenerException | UserNotAuthorizedException e) {
            System.out.println("Ошибка при создании короткой ссылки: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при регистрации: " + e.getMessage());
        }
    }

    /**
     * Метод для парсинга строки времени и преобразования её в Duration.
     * Строки типа "30s", "30m", "30d" преобразуются в Duration.
     *
     * @param input строка, введенная пользователем
     *
     * @return возвращает длительно жизни ссылки, введенную пользователем
     */
    private Duration parseDuration(String input) {
        if (input == null || input.isEmpty()) {
            return Duration.ofSeconds(0);
        }

        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(input.toLowerCase());

        if (matcher.matches()) {
            long amount = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "s":
                    return Duration.ofSeconds(amount);
                case "m":
                    return Duration.ofMinutes(amount);
                case "h":
                    return Duration.ofHours(amount);
                case "d":
                    return Duration.ofDays(amount);
                default:
                    throw new IllegalArgumentException("Неверный формат времени");
            }
        } else {
            throw new IllegalArgumentException("Неверный формат ввода. Используйте формат: 30s, 30m, 30d");
        }
    }

    /**
     * Логин пользователя.
     */
    private void login() {
        System.out.print("Введите UUID пользователя для входа: ");
        String userIdString = scanner.nextLine();
        UUID userId = UUID.fromString(userIdString);

        try {
            shortener.login(userId);
            System.out.println("Вы успешно вошли как: " + userId);
        } catch (Exception e) {
            System.out.println("Ошибка при входе: " + e.getMessage());
        }
    }

    /**
     * Регистрация нового пользователя.
     */
    private void register() {
        try {
            User user = new User();
            UUID userId = shortener.register(user);
            System.out.println("Пользователь зарегистрирован. UUID: " + userId);
        } catch (Exception e) {
            System.out.println("Ошибка при регистрации: " + e.getMessage());
        }
    }

    /**
     * Обновление времени жизни ссылки.
     */
    private void updateLinkExpiration() {
        try {
            System.out.print("Введите короткую ссылку для обновления времени жизни: ");
            String shortLink = scanner.nextLine();
            System.out.print("Введите новое время жизни ссылки (например, 30s для 30 секунд, 30m для 30 минут, 30d для 30 дней): ");
            String timeInput = scanner.nextLine();
            Duration newTimeToLive = parseDuration(timeInput);
            Link link = shortener.updateLinkExpiration(shortLink, newTimeToLive);
            System.out.println("Время жизни ссылки успешно обновлено.");
            System.out.println("Короткая ссылка: " + shortLink);
            System.out.println("Новое время истечения: " + link.getExpireDt());
        } catch (URLShortenerException | UserNotAuthorizedException e) {
            System.out.println("Ошибка при обновлении времени жизни ссылки: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }

    /**
     * Обновление максимального количества кликов для ссылки.
     */
    private void updateLinkMaxClicks() {
        try {
            System.out.print("Введите короткую ссылку для обновления максимального количества кликов: ");
            String shortLink = scanner.nextLine();
            System.out.print("Введите новое максимальное количество кликов для ссылки: ");
            String clicksInput = scanner.nextLine();
            int newMaxClicks = Integer.parseInt(clicksInput);
            Link link = shortener.updateLinkMaxClicks(shortLink, newMaxClicks);
            System.out.println("Максимальное количество кликов для ссылки успешно обновлено.");
            System.out.println("Короткая ссылка: " + shortLink);
            System.out.println("Новое максимальное количество кликов: " + link.getClickCount());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода: Пожалуйста, введите корректное число.");
        } catch (URLShortenerException | UserNotAuthorizedException e) {
            System.out.println("Ошибка при обновлении максимального количества кликов: " + e.getMessage());
        }
    }

    /**
     * Метод для выполнения запроса по короткой ссылке.
     */
    private void requestShortLink() {
        try {
            System.out.print("Введите короткую ссылку: ");
            String shortLink = scanner.nextLine();
            shortener.fetchShortLink(shortLink);
        } catch (UserNotAuthorizedException | URLShortenerException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Удаление ссылки.
     */
    private void deleteLink() {
        try {
            System.out.print("Введите короткую ссылку для удаления: ");
            String shortLink = scanner.nextLine();
            shortener.deleteLink(shortLink);
            System.out.println("Ссылка успешно удалена.");
        } catch (URLShortenerException | UserNotAuthorizedException e) {
            System.out.println("Ошибка при удалении ссылки: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }

    /**
     * Выводит список всех коротких ссылок текущего пользователя с их описанием.
     */
    private void displayUserLinks() {
        try {
            // Получаем список всех ссылок текущего пользователя через сервис
            List<Link> userLinks = shortener.getAllLinksForCurrentUser();

            if (userLinks.isEmpty()) {
                System.out.println("У вас нет созданных коротких ссылок.");
            } else {
                System.out.println("Ваши короткие ссылки:");
                for (Link link : userLinks) {
                    System.out.println("=====================================");
                    System.out.println("Короткая ссылка: " + link.getShortURL());
                    System.out.println("Полная ссылка: " + link.getLongURL());
                    System.out.println("Оставшиеся клики: " + link.getClickCount());
                    System.out.println("Время истечения: " + link.getExpireDt());
                    System.out.println("=====================================");
                }
            }
        } catch (UserNotAuthorizedException e) {
            System.out.println("Вы не авторизованы. Пожалуйста, войдите в аккаунт, чтобы просмотреть свои ссылки.");
        } catch (URLShortenerException e) {
            System.out.println("Ошибка при получении ссылок: " + e.getMessage());
        }
    }

}
