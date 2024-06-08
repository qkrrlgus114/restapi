package com.park.restapi.config;

import com.park.restapi.util.resolver.CurrentMemberArgumentResolver;
import com.park.restapi.util.resolver.CurrentMemberFetchRoleArgumentResolver;
import com.park.restapi.util.resolver.CurrentMemberWriteLockArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CurrentMemberArgumentResolver currentMemberArgumentResolver;
    private final CurrentMemberFetchRoleArgumentResolver currentMemberFetchRoleArgumentResolver;
    private final CurrentMemberWriteLockArgumentResolver currentMemberWriteLockArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberArgumentResolver);
        resolvers.add(currentMemberFetchRoleArgumentResolver);
        resolvers.add(currentMemberWriteLockArgumentResolver);
    }
}
