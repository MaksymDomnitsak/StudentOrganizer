package com.studyorganizer.events.security;

import com.studmodel.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class JwtUser implements UserDetails {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Boolean eventer;
    private final GrantedAuthority authority;

    public JwtUser(Long id,String firstName,String lastName, String email, Boolean eventer, GrantedAuthority authority) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.eventer = eventer;
        this.authority = authority;
    }

    public static JwtUser build(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getUserRole().name());

        return new JwtUser(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getEventer(),
                authority);
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return "";
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEventer() {return eventer;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
