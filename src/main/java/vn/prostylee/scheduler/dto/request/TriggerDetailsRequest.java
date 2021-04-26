package vn.prostylee.scheduler.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TriggerDetailsRequest implements Serializable {

    @NotBlank
    private String name;

    private String group;

    private String cron;

    private LocalDateTime fireTime;
}
