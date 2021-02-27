package vn.prostylee.product.service;

import vn.prostylee.product.dto.response.UsedStatusResponse;

import java.util.List;

public interface UsedStatusService {
    List<UsedStatusResponse> getUsedStatuses();
}
