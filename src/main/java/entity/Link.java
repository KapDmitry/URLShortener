package entity;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс Link представляет сокращенную ссылку, связанную с пользователем.
 *
 * <p>Содержит информацию о длинной и короткой версии URL, идентификаторе пользователя,
 * количестве кликов и времени истечения срока действия.</p>
 */
public class Link {

    /**
     * Уникальный идентификатор ссылки.
     */
    private UUID id;

    /**
     * Длинная версия URL.
     */
    private String longURL;

    /**
     * Сокращенная версия URL.
     */
    private String shortURL;

    /**
     * Идентификатор пользователя, связанного с данной ссылкой.
     */
    private UUID userID;

    /**
     * Количество переходов по сокращенной ссылке.
     */
    private int clickCount;

    /**
     * Дата и время истечения срока действия ссылки.
     */
    private LocalDateTime expireDt;

    /**
     * Дата и время создания ссылки.
     */
    private LocalDateTime createDt;

    /**
     * Конструктор по умолчанию.
     * <p>Генерирует уникальный идентификатор для ссылки.</p>
     */
    public Link() {
        this.id = UUID.randomUUID();
    }

    /**
     * Конструктор с параметрами.
     * <p>Позволяет установить все основные поля ссылки.</p>
     *
     * @param id        уникальный идентификатор ссылки.
     * @param longURL   длинная версия URL.
     * @param shortURL  сокращенная версия URL.
     * @param userID    идентификатор пользователя.
     * @param clickCount количество переходов по ссылке.
     * @param expireDt  дата и время истечения срока действия.
     * @param createDt дата создания записи.
     */
    public Link(UUID id, String longURL, String shortURL, UUID userID, int clickCount, LocalDateTime expireDt, LocalDateTime createDt) {
        this.id = id;
        this.longURL = longURL;
        this.shortURL = shortURL;
        this.userID = userID;
        this.clickCount = clickCount;
        this.expireDt = expireDt;
        this.createDt = createDt;
    }


    /**
     * Возвращает уникальный идентификатор ссылки.
     *
     * @return уникальный идентификатор ссылки.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор ссылки.
     *
     * @param id уникальный идентификатор ссылки.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает длинную версию URL.
     *
     * @return длинная версия URL.
     */
    public String getLongURL() {
        return longURL;
    }

    /**
     * Устанавливает длинную версию URL.
     *
     * @param longURL длинная версия URL.
     */
    public void setLongURL(String longURL) {
        this.longURL = longURL;
    }

    /**
     * Возвращает сокращенную версию URL.
     *
     * @return сокращенная версия URL.
     */
    public String getShortURL() {
        return shortURL;
    }

    /**
     * Устанавливает сокращенную версию URL.
     *
     * @param shortURL сокращенная версия URL.
     */
    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    /**
     * Возвращает идентификатор пользователя, связанного с данной ссылкой.
     *
     * @return идентификатор пользователя.
     */
    public UUID getUserID() {
        return userID;
    }

    /**
     * Устанавливает идентификатор пользователя, связанного с данной ссылкой.
     *
     * @param userID идентификатор пользователя.
     */
    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    /**
     * Возвращает количество переходов по сокращенной ссылке.
     *
     * @return количество переходов по ссылке.
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * Устанавливает количество переходов по сокращенной ссылке.
     *
     * @param clickCount количество переходов по ссылке.
     */
    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    /**
     * Возвращает дату и время истечения срока действия ссылки.
     *
     * @return дата и время истечения срока действия.
     */
    public LocalDateTime getExpireDt() {
        return expireDt;
    }

    /**
     * Устанавливает дату и время истечения срока действия ссылки.
     *
     * @param expireDt дата и время истечения срока действия.
     */
    public void setExpireDt(LocalDateTime expireDt) {
        this.expireDt = expireDt;
    }

    /**
     * Получает дату создания записи ссылки.
     *
     */
    public LocalDateTime getCreateDt() {
        return createDt;
    }
}
