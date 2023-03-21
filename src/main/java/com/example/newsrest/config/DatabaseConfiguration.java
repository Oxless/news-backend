package com.example.newsrest.config;

import com.example.newsrest.util.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//аннотация, помечающая это как класс как конфигурацию
//Spring при старте ищет все классы с этой аннотацией
//и вызываем методы, которые помечены аннотацией Bean.
//Результаты вызовов сохраняет и потом позволяет использовать
//их в других классах
//Они передаются туда автоматически
@Configuration
public class DatabaseConfiguration {

    //Метод, результат которого будет сохранен и может быть использован в другом классе
    //Создаем объект подключения к базе данных
    //База данных PostgreSQL
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        //Указали, что будем использовать PostgreSQL
        dataSource.setDriverClassName("org.postgresql.Driver");
        //Адрес локального сервера базы данных
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        //Пользователь
        dataSource.setUsername("admin");
        //Пароль
        dataSource.setPassword("admin");

        return dataSource;
    }

    //Создаем объект для работы с запросами
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        //При создании объекта создаем две таблицы: counter_table для подсчета кол-ва посещений и users для хранения пользователей
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS counter_table (count INT)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR(128), password VARCHAR(128), session_id VARCHAR(128))");
        return jdbcTemplate;
    }

    //Создаем объект для подсчета посещений
    @Bean
    public Counter counter() {
        return new Counter(jdbcTemplate());
    }

}
