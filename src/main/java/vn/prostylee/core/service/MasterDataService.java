package vn.prostylee.core.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.MasterDataFilter;

/**
 * The base service for master data
 * @param <R> The response type
 */
public interface MasterDataService<R> {

    Page<R> findAll(MasterDataFilter filter);
}
