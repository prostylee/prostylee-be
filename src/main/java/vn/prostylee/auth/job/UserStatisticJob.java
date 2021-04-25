package vn.prostylee.auth.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution // This annotation tells Quartz that a given Job definition (that is, a JobDetail instance) does not run concurrently.
public class UserStatisticJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.debug("UserStatisticJob executing ...");
    }
}
