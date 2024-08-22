package com.watermelon.server.order.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //replace None 사용시 실제 DB사용
@TestPropertySource("classpath:application-local-db.yml")
@DisplayName("[단위] 선착순 레포지토리")
public class OrderEventRepositoryTest
{

    @Autowired
    private OrderEventRepository orderEventRepository;

    @Test
    @DisplayName("전체 삭제")
    public void deleteEventWithQuiz(){
        orderEventRepository.deleteAll();
        Assertions.assertThat(orderEventRepository.findAll().size()).isEqualTo(0);
    }
}
