package com.korit.springboot.security;

import com.korit.springboot.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class PrincipalUser implements UserDetails {

    @Getter
    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getUserRoleEntities().stream().map(userRoleEntity ->
                new SimpleGrantedAuthority(userRoleEntity.getRoleEntity().getRoleName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    public int getUserId() {
        return userEntity.getUserId();
    }

    //굿 애프터눈~~안드로메다 안가고 잘 하고 있었네 꿀잠자야 내가 나중에 편히 깨워주지!
}
