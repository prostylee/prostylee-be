package vn.prostylee.core.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemEntityTest {

    private Long id;

    private String name;

    private String description;

    private Long createdBy;

    private LocalDateTime createdAt;
}
