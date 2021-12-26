package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.store.dto.response.StoreStatisticResponse;
import vn.prostylee.store.entity.StoreStatistic;
import vn.prostylee.store.repository.StoreStatisticRepository;
import vn.prostylee.store.service.StoreStatisticService;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreStatisticServiceImpl implements StoreStatisticService {

    private final StoreStatisticRepository repository;

    @Override
    public StoreStatisticResponse getStoreStatisticByStoreId(Long storeId) {
        StoreStatistic entity = repository.getOne(storeId);
        var response = BeanUtil.copyProperties(entity, StoreStatisticResponse.class);

        response.setNumberOfRating(0L); //TODO get all produc belong to store and count avarage rating.

        return response;
    }
}
