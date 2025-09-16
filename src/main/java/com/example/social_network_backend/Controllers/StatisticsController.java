package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Statistics.ResponseStatisticsDTO;
import com.example.social_network_backend.Facades.StatisticsFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    private final StatisticsFacade statisticsFacade;

    @GetMapping
    public ResponseEntity<ResponseStatisticsDTO> getStatistics(@RequestParam LocalDate startDate,
                                                               @RequestParam LocalDate endDate, Authentication authentication)   {
        return ResponseEntity.ok(statisticsFacade.getStatistics(startDate, endDate, authentication));
    }
}