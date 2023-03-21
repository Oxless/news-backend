package com.example.newsrest.util;

public class Validator {

    /**
     * Проверяет имя пользователя на корректность
     * @param username имя пользователя
     * @return true, если имя пользователя корректно, false - в противном случае
     */
    public static boolean validateUsername(String username) {
        //если длина имени меньше трех
        if(username.length() < 3) {
            return false;
        }
        //если длина имени больше 16
        if(username.length() > 16) {
            return false;
        }
        //если имя не соответствует регулярному выражению,
        //в котором описано, что имя может состоять только из латинских букв, чисел, дефиса, нижнего подчеркивания и точки
        if(!username.matches("^[A-Za-z0-9_.\\-]+$")) {
            return false;
        }
        //если имя начинается не с буквы или цифры
        if(!username.matches("^[A-Za-z0-9].+$")) {
            return false;
        }
        //если все условия корректности выполнены, возвращаем true
        return true;
    }

    /**
     * Проверяет пароль на корректность
     * @param password пароль
     * @return true, если пароль корректен, false - в противном случае
     */
    public static boolean validatePassword(String password) {
        //если длина пароля меньше трех
        if(password.length() < 3) {
            return false;
        }
        //если длина пароля больше 50
        if(password.length() > 50) {
            return false;
        }
        //если пароль не соответствует регулярному выражению,
        //в котором описано, что пароль может состоять только из букв латиницы и кириллицы, чисел, дефиса,
        //нижнего подчеркивания, точки и амперсанда
        if(!password.matches("^[\\w\u0430-\u044fA-Za-z0-9_.&-]+$")) {
            return false;
        }
        return true;
    }

}
