package service;

import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * Конфигурация URLShortener.
 *
 * <p>Содержит параметры настройки, такие как максимальное количество кликов по ссылке
 * и максимальное время жизни ссылки. Загружается из переменных окружения.</p>
 */
public class URLShortenerConfig {

    /**
     * Максимальное число кликов для ссылки.
     */
    private int maxLinkClicks;

    /**
     * Максимальное время жизни ссылки.
     */
    private Duration maxLinkTTL;

    /**
     * Возвращает максимальное количество кликов по ссылке.
     *
     * @return максимальное количество кликов.
     */
    public int getMaxLinkClicks() {
        return maxLinkClicks;
    }

    /**
     * Возвращает максимальное время жизни ссылки.
     *
     * @return максимальное время жизни ссылки.
     */
    public Duration getMaxLinkTTL() {
        return maxLinkTTL;
    }

    /**
     * Загружает конфигурацию из переменных окружения.
     *
     * @throws IllegalArgumentException если переменные окружения не найдены или некорректны.
     */
    public void loadFromEnvironment() throws IllegalArgumentException {
        try {
            String maxClicksEnv = System.getenv("LINK_MAX_CLICKS");
            if (maxClicksEnv == null) {
                throw new IllegalArgumentException("Переменная окружения LINK_MAX_CLICKS не найдена");
            }
            maxLinkClicks = Integer.parseInt(maxClicksEnv);

            String maxTTLEnv = System.getenv("LINK_MAX_TIME_TO_LIVE");
            if (maxTTLEnv == null) {
                throw new IllegalArgumentException("Переменная окружения LINK_MAX_TIME_TO_LIVE не найдена");
            }

            maxLinkTTL = Duration.parse(maxTTLEnv);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректное значение переменной окружения: " + e.getMessage(), e);
        }
    }

}

