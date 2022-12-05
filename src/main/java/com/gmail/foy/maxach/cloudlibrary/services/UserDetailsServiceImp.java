package com.gmail.foy.maxach.cloudlibrary.services;

import com.gmail.foy.maxach.cloudlibrary.exceptions.UserNotFoundException;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import com.gmail.foy.maxach.cloudlibrary.utils.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImp implements UserDetailsService {

    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public UserDetailsImp loadUserByUsername(String id) throws UserNotFoundException {
        User user = userService.getUserById(Long.parseLong(id));
        return UserDetailsImp.convertUserEntityToUserDetailsImpl(user);
    }
}
