package com.yiu.arcap.config;

import com.yiu.arcap.security.TokenAuthenticationFilter;
import com.yiu.arcap.security.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    @Bean
    public SecurityFilterChain filterChain(final @NotNull HttpSecurity http) throws Exception {
        http
                // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
                .httpBasic(HttpBasicConfigurer::disable)
                // JWT 기반이므로 사용안함 | 만약 쿠키 기반이라면 사용
                .csrf(CsrfConfigurer::disable)
                // CORS 설정
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        // Cors 허용 패턴
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(
                                List.of("*")
                        );
                        config.setAllowedMethods(
                                List.of("*")
                        );
                        return config;
                    };
                    c.configurationSource(source);
                })
                // Spring Security 세션 정책: 세션 생성 및 사용 X
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 조건별로 요청 허용/제한 설정
                .authorizeHttpRequests(authorize ->
                        authorize
                                // 회원가입, 로그인은 모두 승인
                                .requestMatchers("/login", "/join", "/refresh", "/token", "/join/authgenerate", "/join/authverify", "/nicknamecheck").permitAll()
                                .anyRequest().authenticated()
                )
                // JWT 인증 필터 적용
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                // 에러 핸들링
                .exceptionHandling(authenticationManager -> authenticationManager
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                                // 인증 문제가 발생했을 때 이부분 호출
                                response.setStatus(401);
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=UTF-8");
                                response.getWriter().write("인증되지 않은 사용자입니다");
                            }
                        })
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
                                    throws IOException, ServletException, IOException {
                                // 권한 문제가 발생했을 때 이부분 호출
                                response.setStatus(403);
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=UTF-8");
                                response.getWriter().write("권한이 없는 사용자입니다");
                            }
                        })
                )
        ;
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                /* swagger v2 */
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                /* swagger v3 */
                "/v3/api-docs/**",
                "/swagger-ui/**");
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
