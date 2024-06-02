package com.park.restapi.config;

import com.park.restapi.util.jwt.JwtFilter;
import com.park.restapi.util.oauth.FailureHandler;
import com.park.restapi.util.oauth.PrincipalOAuth2UserService;
import com.park.restapi.util.oauth.SuccessHandler;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
//@EnableWebSecurity(debug = true)
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
                // csrf 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // cors설정 따름
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // 세션 사용 안함(JWT 사용)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 모든 요청 허용
                .authorizeHttpRequests(authorize ->
                        authorize
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers("/api/authentications/**", "/api/signup",
                        "/api/email-check", "/api/login", "/oauth2/authorization/kakao", "/login/oauth2/code/kakao", "/ws"
                        ,"/api/auth/**").permitAll()
                                .requestMatchers("/api/admin/**", "/api/answers/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/**").hasAuthority("USER")
                                .anyRequest().authenticated()
                )
                // oauth 로그인
                .oauth2Login(oauth -> {
                    oauth.successHandler(successHandler);
                    oauth.failureHandler(failureHandler);
                    // oauth 로그인 성공 후 사용자 정보를 가져오기 위함.
                    // 즉 code -> accessToken 과정을 거친 후 동작.
                    oauth.userInfoEndpoint(userInfo ->
                            userInfo.userService(principalOAuth2UserService)
                    );
                });

        return http.build();
    }


}