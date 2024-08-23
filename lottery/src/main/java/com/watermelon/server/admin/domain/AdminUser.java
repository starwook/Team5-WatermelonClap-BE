package com.watermelon.server.admin.domain;

import com.watermelon.server.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class AdminUser extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    private AdminUser(String uid) {
        this.uid = uid;
    }

    public static AdminUser create(String uid) {
        return new AdminUser(uid);
    }

}
