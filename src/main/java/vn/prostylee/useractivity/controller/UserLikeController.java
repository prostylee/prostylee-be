package vn.prostylee.useractivity.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.useractivity.dto.filter.UserLikeFilter;
import vn.prostylee.useractivity.dto.request.StatusLikeRequest;
import vn.prostylee.useractivity.dto.request.UserLikeRequest;
import vn.prostylee.useractivity.dto.response.UserLikeResponse;
import vn.prostylee.useractivity.service.UserLikeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiVersion.API_V1 + "/user-likes")
public class UserLikeController {

    private final UserLikeService service;

    @GetMapping
    public Page<UserLikeResponse> findAll(UserLikeFilter filter) {
        return service.findAll(filter);
    }

    @GetMapping("/count")
    public long count(UserLikeFilter filter) {
        return service.count(filter);
    }

    @PostMapping("/like")
    public SimpleResponse like(@Valid @RequestBody UserLikeRequest request) {
        return SimpleResponse.builder().data(service.like(request)).build();
    }

    @PutMapping("/unlike")
    public SimpleResponse unlike(@Valid @RequestBody UserLikeRequest request) {
        return SimpleResponse.builder().data(service.unlike(request)).build();
    }

    @PostMapping("/load-status-like")
    public ResponseEntity<SimpleResponse> loadStatusLikes(@Valid @RequestBody StatusLikeRequest statusLikeRequest) {
        List<Long> existIds = service.loadStatusLikes(statusLikeRequest);
        int targetIdListSize = statusLikeRequest.getTargetIds().size();

        if (CollectionUtils.isEmpty(existIds)) {
            return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.NO_CONTENT);
        } else if (existIds.size() < targetIdListSize) {
            return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.PARTIAL_CONTENT);
        }

        return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.OK);
    }

    private SimpleResponse getSimpleResponse(List<Long> existIds) {
        return SimpleResponse.builder().data(existIds).build();
    }

}
