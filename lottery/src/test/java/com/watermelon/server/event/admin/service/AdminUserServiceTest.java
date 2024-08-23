package com.watermelon.server.event.admin.service;

import com.watermelon.server.admin.exception.AdminNotAuthorizedException;
import com.watermelon.server.admin.repository.AdminUserRepository;
import com.watermelon.server.admin.service.AdminUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.watermelon.server.constants.Constants.TEST_NOT_UID;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 어드민 유저 서비스")
class AdminUserServiceTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    @Test
    @DisplayName("어드민 검증 - 실패")
    void authorize() {

        //given
        Mockito.when(adminUserRepository.existsByUid(TEST_NOT_UID)).thenReturn(false);

        //when & then
        Assertions.assertThatThrownBy(() -> adminUserService.authorize(TEST_NOT_UID))
                .isInstanceOf(AdminNotAuthorizedException.class);

    }
}