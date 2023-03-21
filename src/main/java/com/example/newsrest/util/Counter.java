package com.example.newsrest.util;

import org.springframework.jdbc.core.JdbcTemplate;

public class Counter {

    private JdbcTemplate jdbcTemplate;

    public Counter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Увеличивает счетчик посещений
     */
    public void increment() {
        int counter = count(); //получаем исходное количество
        //если оно равно нулю, то через INSERT добавляем запись с нашим счетчиком увеличенным на единицу
        //в противном случае через UPDATE обновляем уже существующую запись со счетчиком
        if(counter == 0) {
            jdbcTemplate.execute("INSERT INTO counter_table VALUES(" + (counter + 1) + ")");
        } else {
            jdbcTemplate.execute("UPDATE counter_table SET count = " + (counter + 1));
        }
    }

    /**
     * Возвращает количество посещений
     * @return количество посещений
     */
    public int count() {
        //получаем значение из базы данных
        return jdbcTemplate.query("SELECT count FROM counter_table", resultSet -> {
            //если в базе данных уже есть какое-то значение счетчика, то возвращаем это значение
            if (resultSet.next()) {
                return resultSet.getInt("count");
            } else {
                //в противном случае возвращаем 0
                return 0;
            }
        });
    }

}
