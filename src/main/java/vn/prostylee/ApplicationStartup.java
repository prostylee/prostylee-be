package vn.prostylee;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.constant.Gender;
import vn.prostylee.auth.dto.request.AccountRequest;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.service.AccountService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Profile({"dev", "test"})
@AllArgsConstructor
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    private final AccountService accountService;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("ApplicationStartup at " + LocalDateTime.now());
        seedData();
    }

    private void seedData() {
        List<Role> roles = roleRepository.findAll();
        if (CollectionUtils.isEmpty(roles)) {
            Role role = Role.builder()
                    .code(AuthRole.SUPER_ADMIN.name())
                    .name("Administrator")
                    .build();
            Role saved = roleRepository.save(role);

            AccountRequest accountRequest = AccountRequest.builder()
                    .username("superadmin1@gmail.com")
                    .password("1234")
                    .active(true)
                    .fullName("Super Admin")
                    .phoneNumber("0900000003")
                    .email("superadmin1@gmail.com")
                    .gender(Gender.MALE.getValue())
                    .allowNotification(true)
                    .roles(Collections.singletonList(saved.getCode()))
                    .build();
            accountService.save(accountRequest);

        }
    }
}