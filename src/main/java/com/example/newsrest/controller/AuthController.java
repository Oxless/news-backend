package com.example.newsrest.controller;

import com.example.newsrest.model.UserAccount;
import com.example.newsrest.service.AuthService;
import com.example.newsrest.util.Counter;
import com.example.newsrest.util.Validator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//RestController обозначает, что мы будем отправлять данные в виде JSON или XML
@RestController
public class AuthController {

    private final AuthService authService;
    private final Counter counter;

    public AuthController(AuthService authService, Counter counter) {
        this.authService = authService;
        this.counter = counter;
    }

    /**
     * Обрабатывает POST-запрос по адресу /register (регистрация)
     * @param username имя пользователя
     * @param password пароль
     * @param response объект ответа с сервера
     * @return
     * - объект пользователя, если регистрация прошла успешно;<br>
     * - invalid_username, если имя пользователя некорректно;<br>
     * - invalid_password, если пароль некорректен;<br>
     * - account_already_exists, если аккаунт уже существует<br>
     */
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        //если имя пользователя не соответствует формату, возвращаем ошибку
        if(!Validator.validateUsername(username)) {
            return ResponseEntity.status(400).body("invalid_username");
        }
        //если пароль не соответствует формату, возвращаем ошибку
        if(!Validator.validatePassword(password)) {
            return ResponseEntity.status(400).body("invalid_password");
        }
        //ищем аккаунт пользователя по имени
        UserAccount userAccount = authService.findAccount(username);
        //если такой уже существует, то мы не можем зарегистрировать новый и отправляем ошибку
        if(userAccount != null) {
            return ResponseEntity.status(401).body("account_already_exists");
        }
        //если такого не существует, то создаем новый
        //и устанавливаем айди сессии в cookie, которые будут отправлены браузеру
        userAccount = authService.createAccount(username, password);
        Cookie sessionId = new Cookie("sessionId", userAccount.getSessionId());
        //указываем, что куки будут действительны в течение суток
        sessionId.setMaxAge(84600);
        response.addCookie(sessionId);
        counter.increment(); //увеличиваем счетчик посещений
        return ResponseEntity.ok(userAccount);
    }

    /**
     * Обрабатывает POST-запрос по адресу /auth (авторизация)
     * @param username имя пользователя
     * @param password пароль
     * @param response объект ответа с сервера
     * @return
     * - объект пользователя, если авторизация прошла успешно;<br>
     * - wrong_password, если пароль неверный;<br>
     * - account_not_exists, если аккаунт не существует<br>
     */
    @PostMapping("/auth")
    public ResponseEntity<Object> authUser(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        //ищем аккаунт пользователя по имени
        UserAccount userAccount = authService.findAccount(username);
        //если такой не существует, то отправляем ошибку
        if(userAccount == null) {
            return ResponseEntity.status(401).body("account_not_exists");
        }
        //если пароль неверный, то тоже отправляем ошибку
        if(!userAccount.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("wrong_password");
        }
        //устанавливаем айди сессии в cookie, которые будут отправлены браузеру
        Cookie sessionId = new Cookie("sessionId", userAccount.getSessionId());
        //указываем, что куки будут действительны в течение суток
        sessionId.setMaxAge(84600);
        response.addCookie(sessionId);
        counter.increment();
        return ResponseEntity.ok(userAccount);
    }

    /**
     * Обрабатывает POST-запрос по адресу /logout (выход из учетной записи)
     * @param sessionId идентификатор сессии
     * @param response объект ответа с сервера
     * @return
     * - имя пользователя, если выход прошел успешно;<br>
     * - account_is_not_authorized, если пользователь был не авторизован
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> logoutUser(@CookieValue(required = false) String sessionId, HttpServletResponse response) {
        //если куки с айди сессии существуют и аккаунт по этому айди найден, то значит, что аккаунт авторизован
        if(sessionId != null) {
            UserAccount userAccount = authService.findAccountBySessionId(sessionId);
            if(userAccount != null) {
                //в таком случае очищаем куки в браузере, чтобы пользователь не был авторизован
                Cookie sessionIdCookie = new Cookie("sessionId", null);
                sessionIdCookie.setMaxAge(0);
                response.addCookie(sessionIdCookie);
                return ResponseEntity.ok(userAccount.getUsername());
            }
        }
        //если пользователь не авторизован, то отправляем ошибку
        return ResponseEntity.status(401).body("account_is_not_authorized");
    }

    /**
     * Обрабатывает GET-запрос по адресу /account (проверка наличия текущей сессии)
     * @param sessionId идентификатор сессии
     * @return
     *  - имя пользователя, если сессия существует;<br>
     *  - ошибку с кодом 401, если пользователь был не авторизован
     */
    @GetMapping("/account")
    public ResponseEntity<String> getAccount(@CookieValue(required = false) String sessionId) {
        if(sessionId != null) {
            //если куки с айди сессии существует
            UserAccount userAccount = authService.findAccountBySessionId(sessionId);
            //и аккаунт по этому айди найден, то возвращаем на фронт, что все нормально и передаем имя пользователя
            if(userAccount != null)
                return ResponseEntity.ok(userAccount.getUsername());
        }
        return ResponseEntity.status(401).build();
    }

}
