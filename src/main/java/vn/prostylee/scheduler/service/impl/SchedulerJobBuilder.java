package vn.prostylee.scheduler.service.impl;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import vn.prostylee.scheduler.dto.request.JobDetailRequest;
import vn.prostylee.scheduler.dto.request.TriggerDetailsRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public final class SchedulerJobBuilder {

    public static final String JOB_ID_KEY = "jobId";
    public static final String JOB_TYPE_KEY = "jobType";
    public static final String JOB_DATA_KEY = "data";
    public static final String JOB_CRON_KEY = "cron";
    public static final String JOB_FIRE_TIME_KEY = "fireTime";

    private SchedulerJobBuilder() {}

    public static JobDetail buildJobDetail(JobDetailRequest jobDetailRequest) throws ClassNotFoundException {
        Map<String, Object> additionalData = Optional.ofNullable(jobDetailRequest.getData()).orElseGet(HashMap::new);
        JobDataMap jobDataMap = new JobDataMap(additionalData);
        jobDataMap.put(JOB_ID_KEY, jobDetailRequest.getJobId());
        jobDataMap.put(JOB_TYPE_KEY, jobDetailRequest.getJobType());
        jobDataMap.put(JOB_DATA_KEY, additionalData);

        Class<? extends Job> jobClazz = (Class<? extends Job>) ClassUtils.getClass(jobDetailRequest.getJobClazz());
        return newJob(jobClazz)
                .withIdentity(buildName(jobDetailRequest.getName()), jobDetailRequest.getGroup())
                .withDescription(jobDetailRequest.getDescription())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public static JobDetail updateJobDetail(JobDetail oldJobDetail, JobDetailRequest jobDetailRequest) {
        Map<String, Object> additionalData = Optional.ofNullable(jobDetailRequest.getData()).orElseGet(HashMap::new);
        JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
        jobDataMap.putAll(additionalData);
        jobDataMap.put(JOB_ID_KEY, jobDetailRequest.getJobId());
        jobDataMap.put(JOB_TYPE_KEY, jobDetailRequest.getJobType());
        jobDataMap.put(JOB_DATA_KEY, additionalData);

        JobBuilder jb = oldJobDetail.getJobBuilder();
        if (StringUtils.isNotBlank(jobDetailRequest.getDescription())) {
            jb.withDescription(jobDetailRequest.getDescription());
        }
        return jb.usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public static Set<Trigger> buildTriggers(List<TriggerDetailsRequest> triggerDetails) {
        return triggerDetails.stream()
                .map(SchedulerJobBuilder::buildTrigger)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Trigger buildTrigger(TriggerDetailsRequest triggerDetailsRequest) {
        if (StringUtils.isNotBlank(triggerDetailsRequest.getCron()) && isValidExpression(triggerDetailsRequest.getCron())) {
            return buildCronSchedule(triggerDetailsRequest);
        }

        if (triggerDetailsRequest.getFireTime() != null) {
            return buildFixedStartTimeSchedule(triggerDetailsRequest);
        }

        throw new IllegalStateException("Unsupported trigger details, triggerDetailsRequest=" + triggerDetailsRequest);
    }

    private static Trigger buildCronSchedule(TriggerDetailsRequest triggerDetailsRequest) {
        return newTrigger()
                .withIdentity(buildName(triggerDetailsRequest.getName()), triggerDetailsRequest.getGroup())
                .withSchedule(
                        cronSchedule(triggerDetailsRequest.getCron())
                                .withMisfireHandlingInstructionFireAndProceed()
                                .inTimeZone(TimeZone.getTimeZone(systemDefault())))
                .usingJobData(JOB_CRON_KEY, triggerDetailsRequest.getCron())
                .build();
    }

    private static Trigger buildFixedStartTimeSchedule(TriggerDetailsRequest triggerDetailsRequest) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_FIRE_TIME_KEY, triggerDetailsRequest.getFireTime());
        return newTrigger()
                .withIdentity(buildName(triggerDetailsRequest.getName()), triggerDetailsRequest.getGroup())
                .withSchedule(simpleSchedule().withMisfireHandlingInstructionNextWithExistingCount())
                .startAt(from(triggerDetailsRequest.getFireTime().atZone(systemDefault()).toInstant()))
                .usingJobData(jobDataMap)
                .build();
    }

    public static JobDetailRequest buildJobDetailRequest(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
        List<TriggerDetailsRequest> triggerDetailsRequestList = triggersOfJob.stream()
                .map(SchedulerJobBuilder::buildTriggerDetails)
                .collect(Collectors.toList());

        return JobDetailRequest.builder()
                .name(jobDetail.getKey().getName())
                .group(jobDetail.getKey().getGroup())
                .description(jobDetail.getDescription())
                .jobClazz(jobDetail.getJobClass().getCanonicalName())
                .jobType(jobDetail.getJobDataMap().getString(JOB_TYPE_KEY))
                .jobId(jobDetail.getJobDataMap().getString(JOB_ID_KEY))
                .data((Map<String, Object>) jobDetail.getJobDataMap().get(JOB_DATA_KEY))
                .triggerDetails(triggerDetailsRequestList)
                .build();
    }

    public static TriggerDetailsRequest buildTriggerDetails(Trigger trigger) {
        return TriggerDetailsRequest.builder()
                .name(trigger.getKey().getName())
                .group(trigger.getKey().getGroup())
                .fireTime((LocalDateTime) trigger.getJobDataMap().get(JOB_FIRE_TIME_KEY))
                .cron(trigger.getJobDataMap().getString(JOB_CRON_KEY))
                .build();
    }

    private static String buildName(String name) {
        return StringUtils.defaultIfBlank(name, UUID.randomUUID().toString());
    }
}
