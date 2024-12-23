package urlgenerator;
/**
 * Интерфейс для генерации коротких ссылок.
 * Этот интерфейс определяет метод для создания короткой версии длинной ссылки.
 * Реализация этого интерфейса будет обеспечивать алгоритм генерации короткой ссылки.
 */
public interface URLGenerator {

    /**
     * Генерирует уникальную короткую ссылку для длинной URL.
     * Этот метод используется для создания короткой ссылки, которая будет
     * использоваться для перенаправления на исходную длинную ссылку.
     *
     * @return Строка, представляющая короткую ссылку, уникальную для каждой генерации
     */
    public String generateShortLink();
}
