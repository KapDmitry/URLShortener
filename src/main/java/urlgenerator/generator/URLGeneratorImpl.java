package urlgenerator.generator;

import urlgenerator.URLGenerator;
import java.security.SecureRandom;

/**
 * Реализация интерфейса {@link URLGenerator} для сокращения URL.
 * <p>
 * Этот класс генерирует короткие ссылки, используя случайную строку из символов и цифр,
 * и предоставляет возможность конфигурировать длину короткой ссылки, префикс и алфавит символов.
 * </p>
 */
public class URLGeneratorImpl implements URLGenerator {

    /**
     * Стандартный алфавит
     */
    private static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Префикс для ссылки.
     */
    private static final String DEFAULT_PREFIX = "https://clck.ru/";

    /**
     * Максимальная длина генерируемой части.
     */
    private static final int DEFAULT_SHORT_URL_LENGTH = 6;

    /**
     * Алфавит для генерации.
     */
    private String alphabet;

    /**
     * Префикс для ссылки.
     */
    private String prefix;

    /**
     * Максимальная длина для генерируемой части..
     */
    private int shortUrlLength;

    /**
     * Генератор случайных чисел.
     */
    private static final SecureRandom random = new SecureRandom();

    /**
     * Конструктор по умолчанию, использующий значения по умолчанию для параметров.
     */
    public URLGeneratorImpl() {
        this.alphabet = DEFAULT_ALPHABET;
        this.prefix = DEFAULT_PREFIX;
        this.shortUrlLength = DEFAULT_SHORT_URL_LENGTH;
    }

    /**
     * Конструктор с параметрами для конфигурирования.
     *
     * @param alphabet     строка, содержащая символы, которые могут быть использованы для генерации короткой ссылки.
     * @param prefix       префикс для короткой ссылки (например, "https://short.ly/").
     * @param shortUrlLength длина короткой ссылки.
     */
    public URLGeneratorImpl(String alphabet, String prefix, int shortUrlLength) {
        this.alphabet = alphabet != null ? alphabet : DEFAULT_ALPHABET;
        this.prefix = prefix != null ? prefix : DEFAULT_PREFIX;
        this.shortUrlLength = shortUrlLength > 0 ? shortUrlLength : DEFAULT_SHORT_URL_LENGTH;
    }

    /**
     * Генерирует короткую ссылку с заданной длиной и префиксом.
     * <p>
     * Генерация происходит путем случайного выбора символов из заданного алфавита.
     * </p>
     *
     * @return сгенерированная короткая ссылка.
     */
    @Override
    public String generateShortLink() {
        StringBuilder shortLink = new StringBuilder(shortUrlLength);

        for (int i = 0; i < shortUrlLength; i++) {
            shortLink.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return prefix + shortLink;
    }

}
