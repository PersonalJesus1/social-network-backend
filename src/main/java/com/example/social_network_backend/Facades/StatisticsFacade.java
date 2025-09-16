package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Statistics.ResponseStatisticsDTO;
import com.example.social_network_backend.Services.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class StatisticsFacade {
    public final StatisticsService statisticsService;

    public ResponseStatisticsDTO getStatistics(LocalDate startDate, LocalDate endDate, Authentication authentication) {
        return statisticsService.getStatistics(startDate, endDate, authentication);
    }
}
