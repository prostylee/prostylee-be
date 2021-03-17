package vn.prostylee.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.statistics.dto.response.UserStatisticsResponse;
import vn.prostylee.statistics.service.StatisticsService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService profileService) {
        this.statisticsService = profileService;
    }

    @GetMapping("/user-activities")
    public UserStatisticsResponse getUserActivities() {
        return statisticsService.getUserActivities();
    }

    @GetMapping("/user-activities/{id}")
    public UserStatisticsResponse getUserActivitiesByUserId(@PathVariable Long userId) {
        return statisticsService.getUserActivitiesByUserId(userId);
    }

}
