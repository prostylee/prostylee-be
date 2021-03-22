package vn.prostylee.useractivity.service;

import vn.prostylee.useractivity.dto.request.MostActiveRequest;

import java.util.List;

public interface UserMostActiveService {

    List<Long> getTargetIdsByMostActive(MostActiveRequest request);
}
