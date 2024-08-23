package com.watermelon.server.event.link.repository;

import com.watermelon.server.event.lottery.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByUri(String uri);

    @Modifying
    @Query("UPDATE Link l SET l.viewCount = l.viewCount + 1 WHERE l.uri = :uri")
    void incrementViewCount(@Param("uri") String uri);

}
