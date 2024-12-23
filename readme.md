# URL Shortener Service

## Описание

Сервис для сокращения URL. Он позволяет пользователям создавать короткие ссылки, управлять ими, а также отслеживать уведомления. Система поддерживает регистрацию пользователей, их авторизацию и возможность изменения параметров ссылок, таких как время жизни и максимальное количество кликов.

---

## Как пользоваться сервисом

### Запуск приложения

Для запуска приложения вам нужно задать две переменные окружения в вашей операционной системе. Переменные окружения указаны в папке ```config``` в ```.env``` файле. Отмечу, что время жизни нужно указывать в формате,
которые может быть распаршен при помощи ```Duration.parse()```, прочитать подробнее можно [здесь](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/Duration.html#parse(java.lang.CharSequence)).
После установки переменных окружения программа сможет корректно работать. 

### Поддерживаемые команды

Когда приложение запущено, в консоли будет отображаться следующее меню:

===== URL Shortener =====

Создать ссылку

Войти в существующий аккаунт

Зарегистрироваться

Изменить время жизни ссылки

Изменить число кликов для ссылки

Сделать запрос по короткой ссылке

Удалить короткую ссылку

Вывести ссылки пользователя

Выйти 

### Доступные команды:

1. **Создать ссылку:**  
   С помощью этой команды вы можете создать короткую ссылку, указав длинный URL, время жизни ссылки и количество кликов.
   Отмечу, что если пользователь не вошел в свой аккаунт система автоматически создаст новый аккаунт и войдет в него, UUID нового пользователя будет выведено на экране.
   Шаг с указанием времени жизни ссылки можно будет пропустить, тогда будет установлено дефолтное значение из конфига. Если же указать время жизни больше чем в конфиге, то
   пользователю будет выведено сообщение о невозможности создать ссылку и ссылка не будет создана, это сделано для того, чтобы система хранения не засорялась потенциально большим 
   числом ссылок с буквально неограниченным временем жизни.
   Также пользователь может указать количество кликов. Здесь можно ввести любое целое положительное значение, даже если оно будет очень большое ссылка все равно удалится
   по истечению времени. Опять же, можно пропустить этот шаг, тогда будет взято число кликов из конфига.
   Можно создавать несколько ссылок для одного пользователя, в частности можно создавать несколько ссылок на один и тот же ресурс и несколько ссылок на одного и того же пользователя.
   Ввод времени жизни или числа кликов можно пропустить нажав ```Enter```.
   Ссылки автоматически удаляются по истечении времени жизни или достижение заданного числа переходов.

2. **Войти в существующий аккаунт:**  
   При выборе этой команды необходимо ввести UUID пользователя для авторизации. Эту команду можно также использовать для переключения между различными аккаунтами.

3. **Зарегистрироваться:**  
   Если пользователь еще не зарегистрирован, можно создать нового пользователя, ему автоматически будет выдан свой UUID.

4. **Изменить время жизни ссылки:**  
   Эта команда позволяет обновить время жизни уже созданной короткой ссылки. 
   Логика работает так, что мы берем ссылку и ставим новое время ее жизни, при этом можно поставить только то время жизни, которое меньше дефолтного. Также стоит отметить,
   что время жизни будет считаться от времени создания ссылки. То есть пользователь может только уменьшить время жизни, но никак не увеличить его.

5. **Изменить число кликов для ссылки:**  
   С помощью этой команды можно изменить количество кликов для короткой ссылки. Можно как уменьшить их число, так и увеличить. 
   Однако, если число кликов уже дошло до 0, то ссылка будет автоматически удалена.

6. **Сделать запрос по короткой ссылке:**  
   С помощью этой команды можно перейти по короткой ссылке. Автоматически будет открыт браузер, также будет уменьшено число доступных кликов.

7. **Удалить короткую ссылку:**  
   Данная команда позволяет удалить уже созданную короткую ссылку.

8. **Вывести ссылки пользователя:**  
   Выводит основную информацию по ссылкам пользователя.

9. **Выйти:**  
   Закрывает приложение.

---

## Пример использования

### 1. Создание короткой ссылки

Когда вы выбираете команду **1. Создать ссылку**, приложение запросит у вас:

- Длинную ссылку
- Время жизни ссылки (например, `30s` для 30 секунд, `30m` для 30 минут, `30d` для 30 дней)
- Количество кликов для ссылки

После ввода всех данных система создаст короткую ссылку и отобразит результат. Также будет создан новый пользователь, если текущий не авторизован.

### 2. Вход в аккаунт

Выберите команду **2. Войти в существующий аккаунт** и введите UUID пользователя для входа. Если пользователь не существует, войти не получится.

### 3. Обновление времени жизни ссылки

С помощью команды **4. Изменить время жизни ссылки** можно обновить время жизни для уже существующей короткой ссылки. Введите короткую ссылку и новое время жизни в нужном формате.

### 4. Обновление числа кликов для ссылки

Выберите команду **5. Изменить число кликов для ссылки** и введите новое максимальное количество кликов для выбранной короткой ссылки.

### 5. Удаление ссылки

Чтобы удалить короткую ссылку, используйте команду **7. Удалить короткую ссылку**, введя короткий URL.

---

## Как протестировать код

1. **Создайте несколько тестовых пользователей**: Для тестирования можно создать несколько пользователей с помощью команды **3. Зарегистрироваться**.

2. **Тестирование создания ссылок**: Создайте несколько длинных ссылок и протестируйте их сокращение с помощью команды **1. Создать ссылку**.

3. **Тестирование входа**: Проверьте работу команды **2. Войти в существующий аккаунт**, вводя корректный UUID.

4. **Тестирование изменения параметров ссылок**: Используйте команды **4. Изменить время жизни ссылки** и **5. Изменить число кликов для ссылки**, чтобы протестировать обновление этих данных.

5. **Тестирование удаления ссылок**: Протестируйте команду **7. Удалить короткую ссылку**.

6. **Тестирование короткой ссылке**: Проверьте переход по короткой ссылке с помощью команды **6. Сделать запрос по короткой ссылке** и убедитесь, что уведомления выводятся корректно. Можно воспользоваться командой **8** и убедиться, что число посещений уменьшилось.
7. **Тестирование уведомлений**: Можно истратить все клики для какой-то ссылке или дождаться когда она протухнет, после ввода очередной команды вы получите уведомление о том, что ссылка удалена. 
Также можно дождаться времени истечения жизни ссылки у другого пользователя, перейти на его аккаунт с помощью команды **2** и убедиться, что уведомление об удалении пришли ему.

---

## Зависимости

Для работы приложения необходимо:

- Java 8 или выше
- Библиотека `java.util` для работы с коллекциями и временем

---

## Примечания

- Вся документация к коду сгенерирована с помощью ```javadoc``` и находится в папке ```docs```, посмотреть ее можно открыв файл ```docs/index.html```.
- Время жизни ссылки можно задать в формате: `30s`, `30m`, `30h`, `30d` (секунды, минуты, часы, дни).
- В случае возникновения ошибок, приложение выведет соответствующие сообщения в консоль.
- Уведомления для текущего пользователя будут автоматически печататься на экране после ввода любой из команд и перед печатью очередного меню выбора. В них будет сообщаться о том, что та или иная ссылка была удалена и будет указана прична удаления.
- Длинные ссылки необходимо вводить с указанием протокола, то есть ```https://....```.
- Все данные хранятся "InMemory", поэтому при повторном запуске текущее состояние будет потеряно.