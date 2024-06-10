package com.park.restapi.config;

import com.park.restapi.util.jwt.JwtFilter;
import com.park.restapi.util.oauth.FailureHandler;
import com.park.restapi.util.oauth.PrincipalOAuth2UserService;
import com.park.restapi.util.oauth.SuccessHandler;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final SuccessHandler successHandler;
    private final FailureHandler failureHandler;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/api/authentications/**", "/api/email/**", "/api/members/login",
                                "/oauth2/authorization/kakao", "/login/oauth2/code/kakao", "/ws").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").hasAnyAuthority("GUEST", "USER", "ADMIN")
                        .requestMatchers("/api/admin/**", "/api/gpt/admin/**", "/api/answers/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/**").hasAuthority("USER")
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .userInfoEndpoint(userInfo -> userInfo.userService(principalOAuth2UserService))
                );
        return http.build();
    }
}