package vn.prostylee.core.controller;

import vn.prostylee.core.service.MasterDataService;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class MasterDataController<R> {

    @Getter
    private final MasterDataService<R> masterDataService;

    public MasterDataController(MasterDataService masterDataService) {
        this.masterDataService = masterDataService;
    }

    @GetMapping
    public List<R> getAll() {
        return masterDataService.findAll();
    }
}
