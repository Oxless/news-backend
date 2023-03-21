package com.example.newsrest.model;

//класс аккаунта пользователя
public class UserAccount {

    //имя
    private final String username;
    //пароль
    private final String password;
    //айди сессии
    private final String sessionId;

    public UserAccount(String username, String password, String sessionId) {
        this.username = username;
        this.password = password;
        this.sessionId = sessionId;
    }

    /**
     * Возвращает имя пользователя
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Возвращает пароль пользователя
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Возвращает идентификатор сессии
     * @return идентификатор сессии
     */
    public String getSessionId() {
        return sessionId;
    }

}
