package vn.prostylee.core.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.service.MasterDataService;

@RequiredArgsConstructor
public class MasterDataController<R> {

    @Getter
    private final MasterDataService<R> masterDataService;

    @GetMapping
    public Page<R> getAll(MasterDataFilter filter) {
        return masterDataService.findAll(filter);
    }
}
