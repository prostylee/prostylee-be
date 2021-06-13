package vn.prostylee.voucher.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.query.NativeQueryResult;
import vn.prostylee.voucher.dto.filter.VoucherUserFilter;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.repository.VoucherExtRepository;
import vn.prostylee.voucher.repository.impl.specification.VoucherSpecificationBuilder;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class VoucherExtRepositoryImpl implements VoucherExtRepository {

    private final EntityManager em;
    private final VoucherSpecificationBuilder spec;

    @Override
    public Page<Voucher> findAll(VoucherUserFilter filter) {

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit());
        NativeQueryResult<Voucher> nativeQueryResult = new NativeQueryResult<>(
                em, Voucher.class, spec.buildQuery(filter), pageable);
        return nativeQueryResult.getResultList(spec.buildParams(filter));
    }
}
