package vn.prostylee.notification.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ApplicationException;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.ThymeleafTemplateProcessor;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.request.PushNotificationTemplateDryRunRequest;
import vn.prostylee.notification.dto.request.PushNotificationTemplateRequest;
import vn.prostylee.notification.dto.response.PushNotificationTemplateResponse;
import vn.prostylee.notification.entity.PushNotificationTemplate;
import vn.prostylee.notification.repository.PushNotificationTemplateRepository;
import vn.prostylee.notification.service.NotificationService;
import vn.prostylee.notification.service.PushNotificationTemplateService;

import java.util.Collections;

@Slf4j
@Service
@AllArgsConstructor
public class PushNotificationTemplateServiceImpl implements PushNotificationTemplateService {

    private final PushNotificationTemplateRepository repository;
    private final BaseFilterSpecs<PushNotificationTemplate> baseFilterSpecs;
    private final NotificationService notificationService;
    private final ThymeleafTemplateProcessor templateProcessor;

    @Override
    public Page<PushNotificationTemplateResponse> findAll(BaseFilter baseFilter) {
        Specification<PushNotificationTemplate> searchable = baseFilterSpecs.search(baseFilter);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<PushNotificationTemplate> page = repository.findAll(searchable, pageable);
        return page.map(this::convertToResponse);
    }

    @Cacheable(value = CachingKey.PUSH_NOTIFICATION_TEMPLATES, key = "#id")
    @Override
    public PushNotificationTemplateResponse findById(Long id) {
        PushNotificationTemplate template = getById(id);
        return convertToResponse(template);
    }

    @Override
    public PushNotificationTemplateResponse save(PushNotificationTemplateRequest request) {
        PushNotificationTemplate template = BeanUtil.copyProperties(request, PushNotificationTemplate.class);
        PushNotificationTemplate savedPushNotificationTemplate = repository.save(template);
        return convertToResponse(savedPushNotificationTemplate);
    }

    private PushNotificationTemplate getById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Push notification template is not found with id [" + id + "]"));
    }

    @CacheEvict(value = CachingKey.PUSH_NOTIFICATION_TEMPLATES, allEntries = true)
    @Override
    public PushNotificationTemplateResponse update(Long id, PushNotificationTemplateRequest request) {
        PushNotificationTemplate template = getById(id);
        BeanUtil.mergeProperties(request, template);
        PushNotificationTemplate savedPushNotificationTemplate = repository.save(template);
        return convertToResponse(savedPushNotificationTemplate);
    }

    @CacheEvict(value = CachingKey.PUSH_NOTIFICATION_TEMPLATES, allEntries = true)
    @Override
    public boolean deleteById(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a Push notification template without existing in database", e);
            return false;
        }
    }

    @Cacheable(value = CachingKey.PUSH_NOTIFICATION_TEMPLATES, key = "#type")
    @Override
    public PushNotificationTemplateResponse findByType(String type) {
        PushNotificationTemplate template = repository.findByType(type).orElseThrow(
                () -> new ResourceNotFoundException("Push notification template is not found with type [" + type + "]"));
        return convertToResponse(template);
    }

    @Override
    public PushNotificationDto dryRun(Long id, PushNotificationTemplateDryRunRequest request) {
        try {
            PushNotificationTemplateResponse templateResponse = findById(id);

            PushNotificationDto pushNotificationRequest = PushNotificationDto.builder()
                    .title(templateProcessor.process(templateResponse.getTitle(), request.getFillData()))
                    .body(templateProcessor.process(templateResponse.getContent(), request.getFillData()))
                    .provider(templateResponse.getType())
                    .data(request.getPushData())
                    .userTokens(Collections.singletonList(UserToken.builder().token(request.getToken()).build()))
                    .build();

            log.info("Dry run request={}", pushNotificationRequest);

            notificationService.sendNotification(pushNotificationRequest);
            return pushNotificationRequest;
        } catch(Exception e) {
            throw new ApplicationException("Can not dry-run for sending an Push notification with id=" + id + " and request=" + request, e);
        }
    }

    private PushNotificationTemplateResponse convertToResponse(PushNotificationTemplate template) {
        return BeanUtil.copyProperties(template, PushNotificationTemplateResponse.class);
    }
}
