package vn.prostylee.auth.service.impl;

import vn.prostylee.auth.constant.AppClientType;
import vn.prostylee.auth.dto.request.AppClientRequest;
import vn.prostylee.auth.dto.response.AppClientResponse;
import vn.prostylee.auth.entity.AppClient;
import vn.prostylee.auth.repository.AppClientRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.service.AppClientService;
import vn.prostylee.core.utils.BeanUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AppClientServiceImpl implements AppClientService {

    private static final int SECRET_KEY_LENGTH = 32;

    private final AppClientRepository appClientRepository;

    @Autowired
    public AppClientServiceImpl(AppClientRepository appClientRepository) {
        this.appClientRepository = appClientRepository;
    }

    @Override
    public AppClientResponse save(AppClientRequest request) {
        AppClient appClient = buildAppClient(Objects.requireNonNull(AppClientType.getByType(request.getType()).orElseGet(() -> null)));
        AppClient requestAppClient = BeanUtil.copyProperties(request, AppClient.class);
        BeanUtil.mergeProperties(requestAppClient, appClient);
        return BeanUtil.copyProperties(appClientRepository.save(appClient), AppClientResponse.class);
    }

    @Override
    public List<AppClientResponse> saveAll(List<AppClient> appClients) {
        return appClients.stream()
                .map(appClientRepository::save)
                .map(appClient -> BeanUtil.copyProperties(appClient, AppClientResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppClient create(AppClientType type) {
        return buildAppClient(type);
    }

    private AppClient buildAppClient(AppClientType type) {
        AppClient appClient = new AppClient();
        appClient.setActive(true);
        appClient.setType(type.getType());
        appClient.setSecretKey(RandomStringUtils.randomAlphanumeric(SECRET_KEY_LENGTH));

        switch (type) {
            case SYS_MOBILE:
                appClient.setName("MOBILE API");
                appClient.setDescription("For mobile app");
                break;
            case SYS_WEB:
                appClient.setName("WEB API");
                appClient.setDescription("For web app");
                break;
            default:
                break;
        }
        return appClient;
    }
}
