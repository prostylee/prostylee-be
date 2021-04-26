package vn.prostylee.scheduler.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import vn.prostylee.scheduler.dto.request.JobDetailRequest;
import vn.prostylee.scheduler.dto.request.TriggerDetailsRequest;
import vn.prostylee.scheduler.service.SchedulerService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for registering the job schedulers was defined in application.yml at the application start up time.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class QuartJobSchedulerStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final SchedulerService schedulerService;
    private final QuartzProperties quartzProperties;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.debug("Registering job schedulers ...");
        try {
            addDynamicJobs();
            log.info("Registered job schedulers successfully!");
        } catch (Exception e) {
            log.error("Could not register job schedulers", e);
        }
    }

    private void addDynamicJobs() {
        quartzProperties.getJobs()
                .stream()
                .map(this::buildJobDetailRequest)
                .collect(Collectors.toList())
                .forEach(request -> {
                    log.debug("Register a job={}", request);
                    schedulerService.createJob(request.getGroup(), request);
                });
    }

    private JobDetailRequest buildJobDetailRequest(QuartzProperties.JobProperties job) {
        return JobDetailRequest.builder()
                .jobId(job.getJobId())
                .group(job.getGroup())
                .jobClazz(job.getJobClazz())
                .jobType(job.getJobType())
                .name(job.getName())
                .description(job.getDescription())
                .data(job.getData())
                .triggerDetails(buildTriggerDetails(job.getDetail()))
                .build();
    }

    private List<TriggerDetailsRequest> buildTriggerDetails(QuartzProperties.JobDetailProperties detail) {
        return Collections.singletonList(TriggerDetailsRequest.builder()
                .name(detail.getName())
                .group(detail.getGroup())
                .cron(detail.getCron())
                .fireTime(detail.getFireTime())
                .build());
    }
}
