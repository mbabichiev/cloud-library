package com.gmail.foy.maxach.cloudlibrary.utils;

import com.gmail.foy.maxach.cloudlibrary.exceptions.UserIsNotAuthException;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@ToString
@Data
public class UserDetailsImp implements UserDetails {

    private Long id;
    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;


    public static UserDetailsImp getUserFromHeaders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new UserIsNotAuthException();
        }
        Object principal = auth.getPrincipal();
        return (UserDetailsImp) principal;
    }

    public static Long getUserIdFromHeaders() {
        return getUserFromHeaders().getId();
    }


    public static String getUserRoleFromHeaders() {
        return getUserFromHeaders().getGrantedAuthorities().stream().findFirst().get().getAuthority();
    }


    public static UserDetailsImp convertUserEntityToUserDetailsImpl(User user) {
        UserDetailsImp userDetailsImp = new UserDetailsImp();
        userDetailsImp.id = user.getId();
        userDetailsImp.login = user.getLogin();
        userDetailsImp.password = user.getPassword();
        userDetailsImp.grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getName()));

        return userDetailsImp;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
}
