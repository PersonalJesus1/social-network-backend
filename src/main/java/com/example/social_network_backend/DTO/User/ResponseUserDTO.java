package com.example.social_network_backend.DTO.User;

import com.example.social_network_backend.Entities.UserRole;
import com.example.social_network_backend.Entities.UserSex;

//Этот класс используется для отправки информации о пользователе обратно клиенту (ответ сервера).
public class ResponseUserDTO {
    private Long userId;
    private String userName;
    private String userSurname;
    private String userEmail;
    private UserSex sex;
    private UserRole userRole;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public UserSex getSex() {
        return sex;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
