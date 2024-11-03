package com.kapuza.springSecurity.services.userService;

import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.repositories.UserRepository;
import com.kapuza.springSecurity.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * проверка наличия юзера в БД во время аутентификации
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> detectedByUserName = userRepository.findByUsername(username);
        log.info("loadUserByUsername --- \u001B[33mПоиск юзера по имени: " + username + "\u001B[0m");
//        detectedByUserName.ifPresent(User::toString);
        if (detectedByUserName.isEmpty()) {
            log.info("loadUserByUsername --- \u001B[31mЮзер: " + username + "  не найден в БД\u001B[0m");
            throw new UsernameNotFoundException("User not Found");
        }
        log.info("loadUserByUsername --- \u001B[33mЮзер:  " + detectedByUserName.get().getUsername() + " найден в БД\u001B[0m");
        return new UserDetailsImpl(detectedByUserName.get());
    }
}