package com.example.social_network_backend.DTO.User;

// CreateUserDTO

import com.example.social_network_backend.Entities.UserSex;

//Этот класс используется для получения данных при создании нового пользователя.
// Клиент отправляет данные на сервер (в теле запроса), и эти данные попадают в объект CreateUserDTO.
public class CreateUserDTO {
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userPassword;
    private UserSex sex;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserSex getSex() {
        return sex;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
    }
}
