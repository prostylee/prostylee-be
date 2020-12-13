package vn.prostylee.core.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import vn.prostylee.core.service.MasterDataService;

import java.util.List;

@RequiredArgsConstructor
public class MasterDataController<R> {

    @Getter
    private final MasterDataService<R> masterDataService;

    @GetMapping
    public List<R> getAll() {
        return masterDataService.findAll();
    }
}
