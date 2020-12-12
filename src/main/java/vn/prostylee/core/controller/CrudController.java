package vn.prostylee.core.controller;

import vn.prostylee.core.dto.filter.BaseFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.service.CrudService;

import javax.validation.Valid;

@RequiredArgsConstructor
public class CrudController<T, R, ID, F extends BaseFilter> {

    @Getter
    private final CrudService<T, R, ID> crudService;

    @GetMapping
    public Page<R> getAll(F baseFilter) {
        return crudService.findAll(baseFilter);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable ID id) {
        return crudService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public R create(@Valid @RequestBody T request) {
        return crudService.save(request);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable ID id, @Valid @RequestBody T request) {
        return crudService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Boolean delete(@PathVariable ID id) {
        return crudService.deleteById(id);
    }
}
