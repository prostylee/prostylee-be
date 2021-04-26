package vn.prostylee.store.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution // This annotation tells Quartz that a given Job definition (that is, a JobDetail instance) does not run concurrently.
public class StoreStatisticJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.debug("StoreStatisticJob executing ....");
    }
}