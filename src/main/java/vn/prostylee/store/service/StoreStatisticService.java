package vn.prostylee.store.service;

import vn.prostylee.store.dto.response.StoreStatisticResponse;

public interface StoreStatisticService {
    StoreStatisticResponse getStoreStatisticByStoreId(Long storeId);
}
