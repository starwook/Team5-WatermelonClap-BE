package com.watermelon.server;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionLoggingAspect {

    private final HikariDataSource dataSource;

    @Before("@annotation(transactional)")
    public void logBeforeTransactionStart(Transactional transactional) throws SQLException {
        HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();
        // 커넥션 정보 로그 출력
        String connectionInfo = String.format("Connection Info - Active: %s, Idle: %s, Total: %s, Wating: %s",
                poolMXBean.getActiveConnections(),
                poolMXBean.getIdleConnections(),
                poolMXBean.getTotalConnections(),
                poolMXBean.getThreadsAwaitingConnection());

        log.info("Transaction started. :{}", connectionInfo);
    }

    @AfterReturning("@annotation(transactional)")
    public void logAfterTransactionCommit(Transactional transactional) {
        HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();
        String connectionInfo = String.format("Connection Info - Active: %s, Idle: %s, Total: %s, Wating: %s",
                poolMXBean.getActiveConnections(),
                poolMXBean.getIdleConnections(),
                poolMXBean.getTotalConnections(),
                poolMXBean.getThreadsAwaitingConnection());
        log.info("Transaction committed.:{}", connectionInfo);
    }
}