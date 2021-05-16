package vn.prostylee.core.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AuditResponse implements Serializable {

    private Long createdBy;

    private Date createdAt;

    private Long updatedBy;

    private Date updatedAt;
}
