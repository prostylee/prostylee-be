package vn.prostylee.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import vn.prostylee.core.configuration.monitor.annotation.UserBehaviorTracking;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.service.CrudService;

public class TrackingCrudController<T, R, ID, F extends BaseFilter> extends CrudController<T, R, ID, F>{

    public TrackingCrudController(CrudService<T, R, ID> crudService) {
        super(crudService);
    }

    @Override
    @UserBehaviorTracking
    public Page<R> getAll(F baseFilter) {
        return super.getAll(baseFilter);
    }

    @Override
    @UserBehaviorTracking
    public R getById(@PathVariable ID id) {
        return super.getById(id);
    }

}
