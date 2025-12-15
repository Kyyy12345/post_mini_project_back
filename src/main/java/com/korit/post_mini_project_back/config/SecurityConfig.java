package com.korit.springboot.config;

import com.korit.springboot.filter.JwtAuthenticationFilter;
import com.mysql.cj.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // IoC에서 자동조립
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //CSR 방식이라서 하는 세팅
    @Bean // Security에서 꼭 세팅 필요
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 모든 세팅들은 다 람다로 세팅 필요

        // 특정 필터전에 추가(뒤에 필터전에 우리가 만든 jwt필터 추가하겠다)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // CORS 적용
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // 세션 비활성화(무상태)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // http 기본 로그인 비활성화(알럿창 로그인)
        http.httpBasic(httpBasic -> httpBasic.disable());
        //form 로그인 비활성화
        http.formLogin(formlogin -> formlogin.disable());
        // csrf 비활성화 (내가 만들어준 페이지에서만 요청이 와야 요청준다) 요까지가 csr때문에 하는거다!!
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/auth/**").permitAll();
            auth.requestMatchers("/v3/api-docs/**").permitAll();
            auth.requestMatchers("/swagger-ui/**").permitAll();
            auth.requestMatchers("/swagger-ui.html").permitAll();
            auth.requestMatchers("/doc").permitAll();
            auth.anyRequest().authenticated();

        });
        //회원가입은 인증이 필요없어야함 - 필터 안 거쳐야함
        //로그인 - 인증 필요없음
        //로그인 완료 시 토큰 지급


        return http.build(); // 예외처리 필요
    }

    @Bean
    // CorsConfigurationSource -> reactive 안붙은거 사용
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); //OPTIONS: 필터, origin 체크할 때
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true); // 모든 쿠키허용


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //   /** : 모든 URL에 요청을 다 허용 -> 프론트엔드에서 서버로 요청을 날릴 수 있다.
        source.registerCorsConfiguration("/**", corsConfiguration); //**모든 요청을 의미
        return source;
    }

    // 외부 라이브러리 - @Component 불가능 -> 수동 @Bean 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}


//레전드.....이거 진짜 어떻게 찾았지???? 이걸 찾아???
//내가 너 완벽 파악했다 생각했는데 간파당한건가....
//일단 약속은 약속이니까! 찬스 드림니다~~