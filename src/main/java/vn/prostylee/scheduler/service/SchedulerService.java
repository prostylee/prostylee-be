package vn.prostylee.scheduler.service;

import vn.prostylee.scheduler.dto.request.JobDetailRequest;
import vn.prostylee.scheduler.dto.response.SchedulerResponse;

import java.util.Optional;

/**
 * This interface defined all behaviors supporting the scheduler management.
 */
public interface SchedulerService {

    /**
     * Create job scheduler response bean.
     *
     * @param jobGroup         the job group
     * @param jobDetailRequest the job detail request bean
     *
     * @return the scheduler response bean
     */
    SchedulerResponse createJob(String jobGroup, JobDetailRequest jobDetailRequest);

    /**
     * Get job scheduler response bean.
     *
     * @param jobGroup the job group
     * @param jobName  the job name
     *
     * @return the scheduler response bean if exists, otherwise null
     */
    SchedulerResponse getJob(String jobGroup, String jobName);

    /**
     * Update job scheduler response bean.
     *
     * @param jobGroup         the job group
     * @param jobName          the job name
     * @param jobDetailRequest the job detail request bean
     *
     * @return the scheduler response bean
     */
    SchedulerResponse updateJob(String jobGroup, String jobName, JobDetailRequest jobDetailRequest);

    /**
     * Delete job scheduler response bean.
     *
     * @param jobGroup the job group
     * @param jobName  the job name
     *
     * @return the scheduler response bean
     */
    SchedulerResponse deleteJob(String jobGroup, String jobName);

    /**
     * Pause job scheduler response bean.
     *
     * @param jobGroup the job group
     * @param jobName  the job name
     *
     * @return the scheduler response bean
     */
    SchedulerResponse pauseJob(String jobGroup, String jobName);

    /**
     * Resume job scheduler response bean.
     *
     * @param jobGroup the job group
     * @param jobName  the job name
     *
     * @return the scheduler response bean
     */
    SchedulerResponse resumeJob(String jobGroup, String jobName);

    /**
     * Find job scheduler response bean.
     *
     * @param jobGroup the job group
     * @param jobName  the job name
     *
     * @return the scheduler response bean
     */
    default Optional<SchedulerResponse> findJob(String jobGroup, String jobName) {
        return Optional.ofNullable(getJob(jobGroup, jobName));
    }

    /**
     * Check whether a job scheduler exists or not.
     *
     * @param jobGroup the job group
     * @param jobName  the job name
     *
     * @return true if exists, otherwise false
     */
    default boolean isExists(String jobGroup, String jobName) {
        return findJob(jobGroup, jobName).isPresent();
    }
}
