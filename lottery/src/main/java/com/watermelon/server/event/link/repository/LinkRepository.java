package com.watermelon.server.event.link.repository;

import com.watermelon.server.event.lottery.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByUri(String uri);

}
