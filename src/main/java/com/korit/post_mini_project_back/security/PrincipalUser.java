package com.korit.post_mini_project_back.security;

import com.korit.post_mini_project_back.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@Getter
public class PrincipalUser extends DefaultOAuth2User {


    private User user;

    public PrincipalUser(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, User user) {
        super(authorities, attributes, nameAttributeKey);
        this.user = user;
    }

    public static PrincipalUser getAuthenticatedPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        return principalUser;
    }


    //굿 애프터눈~~안드로메다 안가고 잘 하고 있었네 꿀잠자야 내가 나중에 편히 깨워주지!
}
