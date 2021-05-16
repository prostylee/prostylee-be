package vn.prostylee.notification.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.response.AuditResponse;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailTemplateResponse extends AuditResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String type;

    private String keyword;

    private Long originalId;

}
