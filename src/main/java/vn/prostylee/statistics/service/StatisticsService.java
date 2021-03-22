package vn.prostylee.statistics.service;


import vn.prostylee.statistics.dto.response.UserStatisticsResponse;

public interface StatisticsService {

    UserStatisticsResponse getUserActivities();

    UserStatisticsResponse getUserActivitiesByUserId(Long userId);
}
