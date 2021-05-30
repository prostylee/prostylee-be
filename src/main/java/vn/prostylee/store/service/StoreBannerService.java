package vn.prostylee.store.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.store.dto.request.StoreBannerRequest;
import vn.prostylee.store.dto.response.StoreBannerResponse;

import java.util.List;

public interface StoreBannerService extends CrudService<StoreBannerRequest, StoreBannerResponse, Long> {

    List<StoreBannerResponse> getListStoreBannerByStore(Long storeId);
}
