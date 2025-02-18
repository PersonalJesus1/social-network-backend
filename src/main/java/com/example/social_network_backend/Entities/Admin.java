package com.example.social_network_backend.Entities;

import jakarta.persistence.*;


@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    private String statisticsRequestedTime;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getStatisticsRequestedTime() {
        return statisticsRequestedTime;
    }

    public void setStatisticsRequestedTime(String statisticsRequestedTime) {
        this.statisticsRequestedTime = statisticsRequestedTime;
    }
}
