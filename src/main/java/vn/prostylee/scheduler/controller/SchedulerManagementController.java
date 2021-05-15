package vn.prostylee.scheduler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.scheduler.dto.request.JobDetailRequest;
import vn.prostylee.scheduler.dto.response.SchedulerResponse;
import vn.prostylee.scheduler.service.impl.SchedulerServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiVersion.API_V1 + "/schedulers")
public class SchedulerManagementController {

    public static final String JOBS = "/job-group/{jobGroup}/jobs";
    public static final String JOBS_BY_NAME = "/job-group/{jobGroup}/jobs/{jobName}";
    public static final String JOBS_TRIGGER = "/job-group/{jobGroup}/jobs/{jobName}/trigger";
    public static final String JOBS_PAUSE = "/job-group/{jobGroup}/jobs/{jobName}/pause";
    public static final String JOBS_RESUME = "/job-group/{jobGroup}/jobs/{jobName}/resume";

    private final SchedulerServiceImpl schedulerService;

    @PostMapping(path = JOBS)
    public SchedulerResponse createJob(@PathVariable String jobGroup, @RequestBody JobDetailRequest jobDetailRequest) {
        return schedulerService.createJob(jobGroup, jobDetailRequest);
    }

    @GetMapping(path = JOBS_BY_NAME)
    public SchedulerResponse findJob(@PathVariable String jobGroup, @PathVariable String jobName) {
        return schedulerService.getJob(jobGroup, jobName);
    }

    @PostMapping(path = JOBS_TRIGGER)
    public SchedulerResponse triggerJob(@PathVariable String jobGroup, @PathVariable String jobName) {
        return schedulerService.triggerJob(jobGroup, jobName);
    }

    @PutMapping(path = JOBS_BY_NAME)
    public SchedulerResponse updateJob(
            @PathVariable String jobGroup,
            @PathVariable String jobName,
            @RequestBody JobDetailRequest jobDetailRequest) {
        return schedulerService.updateJob(jobGroup, jobName, jobDetailRequest);
    }

    @DeleteMapping(path = JOBS_BY_NAME)
    public SchedulerResponse deleteJob(@PathVariable String jobGroup, @PathVariable String jobName) {
        return schedulerService.deleteJob(jobGroup, jobName);
    }

    @PatchMapping(path = JOBS_PAUSE)
    public SchedulerResponse pauseJob(@PathVariable String jobGroup, @PathVariable String jobName) {
        return schedulerService.pauseJob(jobGroup, jobName);
    }

    @PatchMapping(path = JOBS_RESUME)
    public SchedulerResponse resumeJob(@PathVariable String jobGroup, @PathVariable String jobName) {
        return schedulerService.resumeJob(jobGroup, jobName);
    }
}
