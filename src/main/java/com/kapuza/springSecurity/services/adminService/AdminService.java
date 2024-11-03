package com.kapuza.springSecurity.services.adminService;

import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.services.userService.UserDetailsServiceImpl;
import com.kapuza.springSecurity.services.userService.UserService;
import com.kapuza.springSecurity.services.roleService.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AdminService {

    private final UserService userService;
    private final RoleService roleService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(UserService userService,
                        RoleService roleService,
                        UserDetailsServiceImpl userDetailsServiceImpl,
                        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Добавляем в БД при запуске приложения одного админа и одного юзера т.к. БД полностью пустая
     */
    @PostConstruct
    public void addDefaultUsers() {
        log.info("\u001B[33mДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЕЙ ПО УМОЛЧАНИЮ. 1 админ и 1 юзер\u001B[0m");
        try {

            User admin = new User("admin1", 2000);
            User user = new User("Eduard", 2000);

            admin.setPassword(passwordEncoder.encode("admin1"));
            user.setPassword(passwordEncoder.encode("Eduard"));

            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleUser = new Role("ROLE_USER");

            Optional<Role> byRoleNameAdmin = roleService.findByRoleName(roleAdmin.getRoleName());
            Optional<Role> byRoleNameUser = roleService.findByRoleName(roleUser.getRoleName());

            if (byRoleNameAdmin.isEmpty()) {
                roleService.save(roleAdmin);
            }
            if (byRoleNameUser.isEmpty()) {
                roleService.save(roleUser);
            }

            Optional<User> byUserNameAdmin = userService.findByUserName(admin);
            Optional<User> byUserNameUser = userService.findByUserName(user);

            if (byUserNameAdmin.isEmpty()) {
                admin.addRole(roleService.findByRoleName(roleAdmin.getRoleName()).get());
                userService.save(admin);
            }
            if (byUserNameUser.isEmpty()) {
                user.addRole(roleService.findByRoleName(roleUser.getRoleName()).get());
                userService.save(user);
            }

        }catch (Exception e){
            log.info("\u001B[31mЕСТЬ ОШИБКИ при добавлении дефолтных юзеров\u001B[0m");
            System.out.println(e);
        }
    }
}



















//
//
//    @PostConstruct
//    public void addAdmin() {
//        log.info("\u001B[33mДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЕЙ ПО УМОЛЧАНИЮ. 1 админ и 1 юзер\u001B[0m");
//        try {
//
//            User admin = new User("admin1", 2000);
//            User user = new User("Eduard", 2000);
//            admin.setPassword(passwordEncoder.encode("admin1"));
//            user.setPassword(passwordEncoder.encode("Eduard"));
//
//            Role roleAdmin = new Role("ROLE_ADMIN");
//            Role roleUser = new Role("ROLE_USER");
//
//            Optional<Role> byRoleNameAdmin = roleService.findByRoleName(roleAdmin.getRoleName());
//            Optional<Role> byRoleNameUser = roleService.findByRoleName(roleUser.getRoleName());
//            if (byRoleNameAdmin.isEmpty()) {
//                admin.addRole(roleService.save(roleAdmin));
//            }
//            if (byRoleNameUser.isEmpty()) {
//                user.addRole(roleService.save(roleUser));
//            }
//
//            Optional<User> byUserNameAdmin = userService.findByUserName(admin);
//            Optional<User> byUserNameUser = userService.findByUserName(user);
//            if (byUserNameAdmin.isEmpty()) {
//                admin.addRole(roleService.findByRoleName(roleAdmin.getRoleName()).get());
//                userService.save(admin);
//            }
//            if (byUserNameUser.isEmpty()) {
//                user.addRole(roleService.findByRoleName(roleUser.getRoleName()).get());
//                userService.save(user);
//            }
//        }catch (Exception e){
//            log.info("\u001B[31mЕСТЬ ОШИБКИ\u001B[0m");
//            System.out.println(e);
//        }
//    }