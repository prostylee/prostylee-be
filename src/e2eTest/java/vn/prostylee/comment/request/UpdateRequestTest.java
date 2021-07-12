package vn.prostylee.comment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.core.constant.TargetType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestTest {
    private Long targetId;
    private long parentId;
    private TargetType targetType;
    private String content;
    private List<Long> attachmentId;
}
