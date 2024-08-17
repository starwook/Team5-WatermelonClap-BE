package com.watermelon.server.order.repository;

import com.watermelon.server.order.domain.OrderEventWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderEventWinnerRepository extends JpaRepository<OrderEventWinner,Long> {
    Optional<OrderEventWinner> findByApplyTicket(String applyTicket);

}
