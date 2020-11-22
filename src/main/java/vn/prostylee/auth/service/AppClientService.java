package vn.prostylee.auth.service;

import vn.prostylee.auth.constant.AppClientType;
import vn.prostylee.auth.dto.request.AppClientRequest;
import vn.prostylee.auth.dto.response.AppClientResponse;
import vn.prostylee.auth.entity.AppClient;

import java.util.List;

public interface AppClientService {

    AppClientResponse save(AppClientRequest request);

    List<AppClientResponse> saveAll(List<AppClient> appClients);

    AppClient create(AppClientType type);
}
