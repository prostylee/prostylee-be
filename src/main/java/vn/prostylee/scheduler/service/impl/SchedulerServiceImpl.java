package vn.prostylee.scheduler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import vn.prostylee.core.exception.ApplicationException;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.scheduler.dto.request.JobDetailRequest;
import vn.prostylee.scheduler.dto.response.SchedulerResponse;
import vn.prostylee.scheduler.service.SchedulerService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.quartz.JobKey.jobKey;
import static vn.prostylee.scheduler.service.impl.SchedulerJobBuilder.buildJobDetailRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final Scheduler scheduler;

    @Override
    public SchedulerResponse createJob(String jobGroup, JobDetailRequest jobDetailRequest) {
        try {
            jobDetailRequest.setGroup(jobGroup);
            JobDetail jobDetail = SchedulerJobBuilder.buildJobDetail(jobDetailRequest);
            Set<Trigger> triggersForJob = SchedulerJobBuilder.buildTriggers(jobDetailRequest.getTriggerDetails());

            scheduler.scheduleJob(jobDetail, triggersForJob, false);
            log.info("Job created successfully with key={}, jobGroup={}", jobDetail.getKey(), jobGroup);

            return SchedulerResponse.builder().result(jobDetailRequest).build();
        } catch (SchedulerException | ClassNotFoundException | ClassCastException e) {
            String errorMsg = String.format("Could not create a job with jobDetailRequest=%s, error=%s", jobDetailRequest, e.getMessage());
            throw new ApplicationException(errorMsg, e);
        }
    }

    @Override
    public SchedulerResponse getJob(String jobGroup, String jobName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey(jobName, jobGroup));
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey(jobName, jobGroup));

            return Optional.ofNullable(jobDetail)
                    .map(jd -> buildJobDetailRequest(jd, triggersOfJob))
                    .map(jobDetailRequest -> SchedulerResponse.builder().result(jobDetailRequest).build())
                    .orElse(null);
        } catch (SchedulerException e) {
            String errorMsg = String.format("Could not find a job with jobGroup=%s, jobName=%s, error=%s", jobGroup, jobName, e.getMessage());
            throw new ApplicationException(errorMsg, e);
        }
    }

    @Override
    public SchedulerResponse updateJob(String jobGroup, String jobName, JobDetailRequest jobDetailRequest) {
        try {
            Optional<JobDetail> newJobDetail = Optional.ofNullable(scheduler.getJobDetail(jobKey(jobName, jobGroup)))
                    .map(oldJobDetail -> SchedulerJobBuilder.updateJobDetail(oldJobDetail, jobDetailRequest));
            if (newJobDetail.isPresent()) {
                scheduler.addJob(newJobDetail.get(), true);
                log.info("Updated job successfully with key={}, jobGroup={}, jobName={}", newJobDetail.get().getKey(), jobGroup, jobName);

                return SchedulerResponse.builder()
                        .result(jobDetailRequest)
                        .build();
            }
            log.warn("Could not find job to update with jobGroup={}, jobName={}", jobGroup, jobName);
            throw new ResourceNotFoundException(String.format("Job not found with jobGroup=%s, jobName=%s", jobGroup, jobName));
        } catch (SchedulerException e) {
            String errorMsg = String.format("Could not update a job with jobGroup=%s, jobName=%s, jobDetailRequest=%s, error=%s",
                    jobGroup, jobName, jobDetailRequest, e.getMessage());
            throw new ApplicationException(errorMsg, e);
        }
    }

    @Override
    public SchedulerResponse deleteJob(String jobGroup, String jobName) {
        try {
            scheduler.deleteJob(jobKey(jobName, jobGroup));
            return SchedulerResponse.builder()
                    .result(String.format("Deleted a job successfully with jobGroup=%s, jobName=%s", jobGroup, jobName))
                    .build();
        } catch (SchedulerException e) {
            String errorMsg = String.format("Could not delete a job with jobGroup=%s, jobName=%s, error=%s", jobGroup, jobName, e.getMessage());
            throw new ApplicationException(errorMsg, e);
        }
    }

    @Override
    public SchedulerResponse pauseJob(String jobGroup, String jobName) {
        try {
            scheduler.pauseJob(jobKey(jobName, jobGroup));
            return SchedulerResponse.builder()
                    .result(String.format("Paused a job successfully with jobGroup=%s, jobName=%s", jobGroup, jobName))
                    .build();
        } catch (SchedulerException e) {
            String errorMsg = String.format("Could not pause a job with jobGroup=%s, jobName=%s, error=%s", jobGroup, jobName, e.getMessage());
            throw new ApplicationException(errorMsg, e);
        }
    }

    @Override
    public SchedulerResponse resumeJob(String jobGroup, String jobName) {
        try {
            scheduler.resumeJob(jobKey(jobName, jobGroup));
            return SchedulerResponse.builder()
                    .result(String.format("Resumed job successfully with jobGroup=%s, jobName=%s", jobGroup, jobName))
                    .build();
        } catch (SchedulerException e) {
            String errorMsg = String.format("Could not resume a job with jobGroup=%s, jobName=%s, error=%s", jobGroup, jobName, e.getMessage());
            throw new ApplicationException(errorMsg, e);
        }
    }
}