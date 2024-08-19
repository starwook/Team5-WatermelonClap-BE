package com.watermelon.server.common.config;

import com.watermelon.server.admin.interceptor.AdminAuthorizationInterceptor;
import com.watermelon.server.auth.interceptor.LoginCheckInterceptor;
import com.watermelon.server.auth.resolver.UidArgumentResolver;
import com.watermelon.server.common.interceptor.LoginCheckExpectationPostOnlyInterceptor;
import com.watermelon.server.event.lottery.interceptor.FirstLoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.watermelon.server.common.constants.PathConstants.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UidArgumentResolver uidArgumentResolver;
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
    private final LoginCheckExpectationPostOnlyInterceptor loginCheckExpectationPostOnlyInterceptor;
    private final FirstLoginInterceptor firstLoginInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //uid 관련 ArgumentResolver 등록
        resolvers.add(uidArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns(
                        LOTTERIES_INFO,
                        LOTTERIES_RANK,
                        PARTS,
                        PARTS_EQUIP,
                        PARTS_REMAIN,
                        MY_LINK,
                        "/admin/**"
                );

        registry.addInterceptor(loginCheckExpectationPostOnlyInterceptor)
                        .order(2)
                                .addPathPatterns(EXPECTATIONS);

        registry.addInterceptor(firstLoginInterceptor)
                .order(3)
                .addPathPatterns(
                        LOTTERIES_INFO,
                        LOTTERIES_RANK,
                        PARTS,
                        PARTS_EQUIP,
                        PARTS_REMAIN,
                        MY_LINK,
                        "/admin/**"
                );

        registry.addInterceptor(adminAuthorizationInterceptor)
                .order(4)
                .addPathPatterns("/admin/**");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("http://localhost:5173")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
//                .allowedHeaders("*")
//                .maxAge(3600);
//    }

}
