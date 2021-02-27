package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.util.CollectionUtils;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.response.UsedStatusResponse;
import vn.prostylee.product.entity.UsedStatus;
import vn.prostylee.product.repository.UsedStatusRepository;
import vn.prostylee.product.service.UsedStatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedStatusServiceImpl implements UsedStatusService {

    private final UsedStatusRepository usedStatusRepository;

    @Override
    public List<UsedStatusResponse> getUsedStatuses() {
        List<UsedStatus> entities =  usedStatusRepository.findAll();
        if(CollectionUtils.isEmpty(entities))
            throw new ResourceNotFoundException("Used Status data is not exists, Please insert master data");
        return BeanUtil.listCopyProperties(entities, UsedStatusResponse.class);
    }
}
