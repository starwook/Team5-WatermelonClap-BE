package com.watermelon.server.order.repository;

import com.watermelon.server.order.result.domain.OrderResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderResultRepository extends JpaRepository<OrderResult, Long> {

}
