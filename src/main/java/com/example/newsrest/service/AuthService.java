package com.example.newsrest.service;

import com.example.newsrest.model.UserAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

//экземпляр класса с аннотацией Service создастся сам
//и будет доступен для использования в других классах
@Service
public class AuthService {

    //класс для работы с запросами
    private final JdbcTemplate jdbcTemplate;

    public AuthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Создает аккаунт пользователя
     * @param username имя пользователя
     * @param password пароль
     * @return новый аккаунт пользователя
     */
    public UserAccount createAccount(String username, String password) {
        //переводим имя пользователя в нижний регистр, т.к. будем именно так хранить его в бд
        //для того, чтобы пользователи с одним и тем же именем, но в разном регистре, считались за одного и того же
        username = username.toLowerCase();
        //создаем объект аккаунта
        //UUID.randomUUID().toString() - возвращает случайную строку
        UserAccount userAccount = new UserAccount(username, password, UUID.randomUUID().toString());
        //записываем данные аккаунта в базу данных
        jdbcTemplate.execute("INSERT INTO users (username, password, session_id) VALUES ('" + username + "', '" + password + "', '" + userAccount.getSessionId() + "')");
        return userAccount;
    }

    /**
     * Ищет аккаунт пользователя по его имени
     * @param username имя пользователя
     * @return аккаунт пользователя, если он существует, иначе - null
     */
    public UserAccount findAccount(String username) {
        //ищем аккаунт в базе данных по имени пользователя
        return jdbcTemplate.query("SELECT * FROM users WHERE username = '" + username.toLowerCase() + "'", resultSet -> {
            //если такой аккаунт существует, то resultSet.next() вернет true и мы создадим новый объект с данными пользователя
            //в противном случае вернем null
            if(resultSet.next()) {
                return new UserAccount(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("session_id"));
            } else {
                return null;
            }
        });
    }

    /**
     * Ищет аккаунт пользователя по его идентификатору сессии
     * @param sessionId идентификатор сессии
     * @return аккаунт пользователя, если он существует, иначе - null
     */
    public UserAccount findAccountBySessionId(String sessionId) {
        //ищем аккаунт в базе данных по сессии
        return jdbcTemplate.query("SELECT * FROM users WHERE session_id = '" + sessionId.toLowerCase() + "'", resultSet -> {
            //если такой аккаунт существует, то resultSet.next() вернет true и мы создадим новый объект с данными пользователя
            //в противном случае вернем null
            if(resultSet.next()) {
                return new UserAccount(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("session_id"));
            } else {
                return null;
            }
        });
    }

}
