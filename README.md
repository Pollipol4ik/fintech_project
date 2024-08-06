# fintech_project
***
ФИО: Купцова Полина Дмитриевна
***
Веб-приложение на языке программирования `Java` с использованием `Spring Boot 3` для перевода набора слов на другой язык с использованием сервиса перевода Yandex.

* Приложение принимает на вход строку, состоящую из набора слов, исходный язык и целевой язык в качестве параметров для перевода.
В ответе программа должна вернуть переведенную строку.
* Каждое слово переводиться отдельно в нескольких потоках. При этом число одновременно работающих потоков не превышает 10.
* Приложение сохраняет в реляционную базу данных информацию о запросе: IP-адрес пользователя, входную строку для перевода и результат перевода.

Реализованы 2 таблицы для хранения данных:
* `ip_address` хранит в себе информацию о уникальных ip-адресах пользователей;
* `translation_request` хранит в себе информацию о идентификаторе ip-дреса пользователя, тексте ввода и переведенном тексте.

# Запуск программы
***
Для работы данного приложения изначально необходимо перейти по ссылке для получения IAM-токена и folderId ([документация по работе с Translate](https://yandex.cloud/ru/docs/translate/quickstart#before-begin)).
Затем вписать файл `application.yml` в поля `api-key` и `folder-id` полученные данные.

Для демонстрации работы можно запустить готовый compose.yml файл из корня проекта. Никакой конфигурации при этом прописывать не нужно.
```
docker compose up 
```

Далее запускаем проект и переходим по [ссылке](http://localhost:8080/api/v1/swagger-ui/index.html#/translation-controller/translate).
Пример ввода:
```{
"sourceLanguageCode": "en",
"targetLanguageCode": "ru",
"sourceText": "Hello world, this is my first program"
}
```

Пример вывода при этих входных данных:
```{
"translatedText": "Здравствуйте мир, этот является мой первый программа"
}
```
***
P.S: Проповала использовать Bucket4j для ограничения количества запросов к Yandex api, но использование Semaphore оказалось эффективнее(быстрее).

P.P.S: Добавила github actions для покрытия тестами(jacoco), но забыла, что они для Pull request-ов)