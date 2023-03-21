package com.example.newsrest.model;

//класс новостного поста
public class NewsPost {

    //время в миллисекундах, прошедшее с 01.01.1970 (по-другому timestamp)
    private long date;
    //заголовок
    private String header;
    //текст
    private String text;

    public NewsPost(long date, String header, String text) {
        this.date = date;
        this.header = header;
        this.text = text;
    }

    /**
     * Возвращает дату создания поста
     * @return дата создания в виде timestamp
     */
    public long getDate() {
        return date;
    }

    /**
     * Возвращает заголовок поста
     * @return заголовок поста
     */
    public String getHeader() {
        return header;
    }

    /**
     * Возвращает текст поста
     * @return текст поста
     */
    public String getText() {
        return text;
    }

}
