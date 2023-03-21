package com.example.newsrest.controller;

import com.example.newsrest.model.NewsPost;
import com.example.newsrest.model.UserAccount;
import com.example.newsrest.service.AuthService;
import com.example.newsrest.util.Counter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

//RestController обозначает, что мы будем отправлять данные в виде JSON или XML
@RestController
public class NewsController {

    //форматтер для даты
    //формат будет выглядеть как 01.01.1970 00:00
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private final Counter counter;
    private final AuthService authService;

    public NewsController(Counter counter, AuthService authService) {
        this.counter = counter;
        this.authService = authService;
    }

    /**
     * Обрабатывает GET-запрос по адресу /news
     * @param sessionId идентификатор сессии
     * @return список новостей, если пользователь авторизован, иначе - ошибку
     */
    @GetMapping("/news")
    public ResponseEntity<List<NewsPost>> newsPage(@CookieValue(required = false) String sessionId) {
        //если куки с айди сессии не существует, то возвращаем ошибку доступа
        if(sessionId == null) {
            return ResponseEntity.status(401).build();
        }
        UserAccount accountBySessionId = authService.findAccountBySessionId(sessionId);
        //если куки существует, но по этому айди не найден аккаунт, то возвращаем такую же ошибку
        if(accountBySessionId == null) {
            return ResponseEntity.status(401).build();
        }
        //если все хорошо, то возвращаем список объектов - новостных постов
        return ResponseEntity.ok(List.of(
                new NewsPost(
                        System.currentTimeMillis(),
                        "Какой-то несмешной заголовок на несмешном сайте.",
                        "Это пост для токсиков. Тут нет ничего смешного"
                ),
                new NewsPost(
                        System.currentTimeMillis() + 1500*50,
                        "Еще один несмешной заголовок на несмешном сайте.",
                        "Это еще один пост для токсиков. Тут тоже нет ничего смешного"
                ),
                new NewsPost(
                        System.currentTimeMillis() + 1500*50,
                        "Еще один несмешной заголовок на несмешном сайте.",
                        "Это еще один пост для токсиков. Тут тоже нет ничего смешного"
                ),
                new NewsPost(
                        System.currentTimeMillis() + 1500*50,
                        "Еще один несмешной заголовок на несмешном сайте.",
                        "Это еще один пост для токсиков. Тут тоже нет ничего смешного"
                ),
                new NewsPost(
                        System.currentTimeMillis() + 1500*50,
                        "Еще один несмешной заголовок на несмешном сайте.",
                        "Это еще один пост для токсиков. Тут тоже нет ничего смешного"
                )
        ));
    }

    /**
     * Обрабатывает GET-запрос по адресу /info
     * @return дополнительные данные - количество посещений и дата входа в формате {counter: $count, date: $date}
     */
    @GetMapping("/info")
    public Map<String, Object> infoPage() {
        int count = counter.count();
        //создаем структуру вида
        //counter: кол-во
        //date: дата
        return Map.of("counter", count, "date", DATE_FORMAT.format(new Date(System.currentTimeMillis())));
    }

}
