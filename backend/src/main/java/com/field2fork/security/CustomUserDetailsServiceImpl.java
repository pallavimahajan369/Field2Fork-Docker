package com.field2fork.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.field2fork.dao.UserDao;
import com.field2fork.pojos.User;

@Service
@Transactional
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
        return new CustomUserDetailsImpl(userEntity);
    }
}