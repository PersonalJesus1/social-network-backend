Social Network API

📌 Описание

Этот проект представляет собой REST API для социальной сети, разработанный на Spring Boot с использованием PostgreSQL и JWT-аутентификации.

⚙️ Технологии

Java 17

Spring Boot 3

Spring Security (JWT)

Hibernate (JPA)

PostgreSQL

Lombok

Postman (для тестирования API)

🚀 Запуск проекта

1️⃣ Клонирование репозитория

git clone https://github.com/PersonalJesus1/social-network-backend
cd social-network-api

2️⃣ Настройка базы данных

Создай базу в PostgreSQL и обнови application.yml:

spring:
datasource:
url: jdbc:postgresql://localhost:5432/local-network
username: postgres
password: postgresql

3️⃣ Запуск приложения

mvn spring-boot:run

🛠 API Эндпоинты

🔑 Аутентификация

POST /api/v1/auth/register — Регистрация пользователя

POST /api/v1/auth/login — Получение JWT-токена

👤 Пользователи

GET /api/v1/users — Получить всех пользователей

GET /api/v1/users/{id} — Получить пользователя по ID

PUT /api/v1/users/{id} — Обновить данные пользователя

DELETE /api/v1/users/{id} — Удалить пользователя

💬 Сообщения

POST /api/v1/messages — Отправить сообщение

GET /api/v1/messages/user/{userId} — Получить сообщения пользователя

DELETE /api/v1/messages/{id} — Удалить сообщение

📌 ERD-диаграмма

🔗 Посмотреть ERD-диаграмму:
https://lucid.app/lucidchart/ad221f1d-1f4d-45b3-9bf3-be0b2593fb19/edit?viewport_loc=322%2C-150%2C1316%2C891%2C0_0&invitationId=inv_99919642-5db5-4478-bf4b-22c6e18a6137


📩 Тестирование в Postman

Открыть Postman

Импортировать коллекцию API (SocialNetworkBackendCollection.postman_collection.json)

Выполнить запросы

📝 TODO



📌 Автор

👩‍💻 Yulia📧 Email: yulia320455@gmail.com💼 GitHub: PersonalJesus1

