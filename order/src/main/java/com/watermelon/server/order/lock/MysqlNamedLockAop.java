package com.watermelon.server.order.lock;


import com.watermelon.server.order.repository.OrderApplyResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

//NamedLock 선언시 수행되는 AOP Class
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MysqlNamedLockAop {
    private final OrderApplyResultRepository orderApplyResultRepository;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.watermelon.server.order.lock.MysqlNamedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //현재 실행중인 method 객체를 가져온다
        Method method = methodSignature.getMethod();
        MysqlNamedLock mysqlNamedLock = method.getAnnotation(MysqlNamedLock.class);
        String key = mysqlNamedLock.key();
        int waitTime = mysqlNamedLock.waitTime();
        try{ //일단은 한 번만 시도
            orderApplyResultRepository.getLock(key,waitTime);
            //어노테이션이 달린 함수 실행
            return aopForTransaction.proceed(joinPoint);
        }
        finally {
            try{
                orderApplyResultRepository.releaseLock(key);
            }
            catch (Exception e){ //Lock이 없음애도 해제 시도
                e.printStackTrace();
                log.error("Lock cant be UnLock");
            }
        }
    }
}
