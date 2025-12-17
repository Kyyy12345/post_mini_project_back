package com.korit.post_mini_project_back.filter;

import com.korit.post_mini_project_back.entity.User;
import com.korit.post_mini_project_back.jwt.JwtTokenProvider;
import com.korit.post_mini_project_back.mapper.UserMapper;
import com.korit.post_mini_project_back.security.PrincipalUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// 회원가입, 로그인, 토큰발행해야 이밑에 작업할 수 있다
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //OncePerRequestFilter - 인증은 한번만 일어나면 됨 - 한 요청당 jwt 한번만 확인하겠다

    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 팔찌를 안 채워주고 넘기는 행위
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = bearerToken.replaceAll("Bearer ", "");

        if(!jwtTokenProvider.validateToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        int userId = jwtTokenProvider.getUserId(accessToken);
        User foundUser = userMapper.findByUserId(userId);

        if (foundUser == null) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(foundUser);

        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(foundUser.getRole()));
        PrincipalUser principalUser = new PrincipalUser(authorities, Map.of("id", foundUser.getOauth2Id()), "id", foundUser);
        String password = "";

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalUser, password, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication); // 팔찌 채워주기
        filterChain.doFilter(request, response);
    }
    //삐졌어~??? 미안해~~ 진짜 다 적고 밑에 고민중이어씀!!!!
}
