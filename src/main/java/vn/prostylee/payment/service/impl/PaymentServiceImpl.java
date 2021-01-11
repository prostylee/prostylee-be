package vn.prostylee.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.payment.dto.response.PaymentResponse;
import vn.prostylee.payment.entity.PaymentType;
import vn.prostylee.payment.repository.PaymentTypeRepository;
import vn.prostylee.payment.service.PaymentService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BaseFilterSpecs<PaymentType> baseFilterSpecs;

    private final PaymentTypeRepository paymentTypeRepository;

    @Override
    public Page<PaymentResponse> findAll(MasterDataFilter filter) {
        Specification<PaymentType> searchable = baseFilterSpecs.search(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<PaymentType> page = paymentTypeRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, PaymentResponse.class));
    }
}
