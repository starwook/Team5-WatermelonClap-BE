package com.watermelon.server.event.order.repository;

import com.watermelon.server.event.order.domain.OrderEventWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderEventWinnerRepository extends JpaRepository<OrderEventWinner,Long> {
    Optional<OrderEventWinner> findByApplyTicket(String applyTicket);

}
