package com.github.springbootdemo.demo.service;

import com.github.springbootdemo.demo.entity.User;
import com.github.springbootdemo.demo.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Inject
    public UserService(
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserMapper userMapper
    ) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        // save("laowang", "laowang");
    }

    public void save(String username, String password) {
        userMapper.save(username,bCryptPasswordEncoder.encode(password));
    }

    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.一定要加密
        // 2.加密是不可逆的
        // 3.加密必须是一致的
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + "不存在");
        }
        return new org.springframework.security.core.userdetails.User(username, user.getEncryptedPassword(), Collections.emptyList());
    }
}
