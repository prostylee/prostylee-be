package vn.prostylee.scheduler.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;
import vn.prostylee.scheduler.dto.request.JobDetailRequest;
import vn.prostylee.scheduler.service.impl.SchedulerJobBuilder;

import java.util.Collections;

@Slf4j
@Component
public class CustomJobListenerSupport extends JobListenerSupport {

    @Override
    public String getName() {
        return CustomJobListenerSupport.class.getCanonicalName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("jobToBeExecuted key={}, jobDetailRequest={}", context.getJobDetail().getKey(), getJobInfo(context));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("jobExecutionVetoed key={}, jobDetailRequest={}", context.getJobDetail().getKey(), getJobInfo(context));
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info("jobWasExecuted key={}, jobDetailRequest={}", context.getJobDetail().getKey(), getJobInfo(context));
        if (jobException != null) {
            log.error("jobWasExecuted with exception ", jobException);
        }
    }

    private JobDetailRequest getJobInfo(JobExecutionContext context) {
        return SchedulerJobBuilder.buildJobDetailRequest(context.getJobDetail(), Collections.singletonList(context.getTrigger()));
    }
}