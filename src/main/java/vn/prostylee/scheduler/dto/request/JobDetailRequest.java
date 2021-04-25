package vn.prostylee.scheduler.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JobDetailRequest implements Serializable {

    @NotBlank
    private String name;

    private String group;

    private String description;

    /**
     * The class name implementation from a {@link org.quartz.Job} interface
     */
    @NotBlank
    private String jobClazz;

    @NotBlank
    private String jobId;

    @NotBlank
    private String jobType;

    @Builder.Default
    private Map<String, Object> data = new HashMap<>();

    @Builder.Default
    private List<TriggerDetailsRequest> triggerDetails = new ArrayList<>();

}
