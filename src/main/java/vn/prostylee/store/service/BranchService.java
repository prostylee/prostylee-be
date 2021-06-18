package vn.prostylee.store.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.store.dto.request.BranchRequest;
import vn.prostylee.store.dto.response.BranchResponse;
import vn.prostylee.store.entity.Branch;

public interface BranchService extends CrudService<BranchRequest, BranchResponse, Long> {

    Branch getById(Long id);
}
