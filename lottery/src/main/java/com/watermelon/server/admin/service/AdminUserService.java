package com.watermelon.server.admin.service;

import com.watermelon.server.admin.domain.AdminUser;
import com.watermelon.server.admin.exception.AdminNotAuthorizedException;
import com.watermelon.server.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    public void authorize(String uid){
        if(!adminUserRepository.existsByUid(uid)) throw new AdminNotAuthorizedException();
    }

    @Profile("!deploy")
    @PostConstruct
    public void registrationAdmin(){
        adminUserRepository.save(AdminUser.create("TEST_UID"));
    }

}
