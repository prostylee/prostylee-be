package vn.prostylee.useractivity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MostActiveRequest {

    private List<String> targetTypes;

    private int limit;

    private Date fromDate;

    private Date toDate;
}
