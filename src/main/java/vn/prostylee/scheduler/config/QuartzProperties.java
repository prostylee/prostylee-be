package vn.prostylee.scheduler.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("app.quartz")
public class QuartzProperties {

    private boolean enable;

    private List<JobProperties> jobs;

    @Data
    public static class JobProperties {

        private String name;

        private String group;

        private String description;

        private String jobClazz;

        private String jobId;

        private String jobType;

        private Map<String, Object> data;

        private JobDetailProperties detail;
    }

    @Data
    public static class JobDetailProperties {

        private String name;

        private String group;

        private String cron;

        private LocalDateTime fireTime;
    }

}
