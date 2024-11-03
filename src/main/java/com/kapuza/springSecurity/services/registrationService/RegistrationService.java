package com.kapuza.springSecurity.services.registrationService;

import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.security.UserDetailsImpl;
import com.kapuza.springSecurity.services.userService.UserServiceImpl;
import com.kapuza.springSecurity.services.roleService.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegistrationService {

    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public RegistrationService(UserServiceImpl userServiceImpl,
                               PasswordEncoder passwordEncoder,
                               RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.roleServiceImpl = roleServiceImpl;
    }

    //    @Transactional
    public User register(User user) {
        log.info("\u001B[33m Регистрируемый пользователь в начале метода регистрации: " + user + "\u001B[0m");
        /**Получаем текущего пользователя в системе*/
        User currentUserInSystem = UserDetailsImpl.getUser();


        /**Списку ролей устанавливаем одну роль по умолчанию - USER.
         * Всё это для того, чтобы роль USER назначить всем при регистрации через форму регистрации.
         * Т.к. Остальные роли может назначить только ADMIN или другой у кого есть такие права;
         * роль ROLE_USER и ROLE_ADMIN создаются при поднятии БД*/
        for (Role role : currentUserInSystem.getRoles()) {
            /** Если админ создаёт пользователя, то просто сохраняем тот список ролей, который пришёл попутно взяв ID этих ролей из БД*/
            if (role.getRoleName().equals("ROLE_ADMIN")) {
                log.info("\u001B[33m Админ регистрирует пользователя " + user + "\u001B[0m");
                adminSaveInputListRoles(user);
            }else {
                defaultSaveInputListRoles(user);
                log.info("\u001B[33m Регистрация пользователя через форму регистрации " + user + "\u001B[0m");
            }
        }
        /**шифруем пароль, который ввёл в форме юзер*/
        String encodePassword = passwordEncoder.encode(user.getPassword());
        /**устанавливаем шифрованный пароль юзеру*/
        user.setPassword(encodePassword);

        log.info("\u001B[33m Регистрируемый пользователь в конце метода регистрации: " + user + "\u001B[0m");
        return userServiceImpl.save(user);
    }

    private void defaultSaveInputListRoles(User user) {
        Role saveUserRole = new Role("ROLE_USER");
        Optional<Role> byRoleName = roleServiceImpl.findByRoleName(saveUserRole.getRoleName());
        byRoleName.ifPresent(value -> user.addRole(byRoleName.get()));
    }

    private void adminSaveInputListRoles(User user) {
        if(user.getRoles().isEmpty()){
            defaultSaveInputListRoles(user);
            return;
        }
        List<Role> listToSave = new ArrayList<>();
        user.getRoles().stream()
                .map(role -> roleServiceImpl.findByRoleName(role.getRoleName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(listToSave::add);
        user.setRoles(listToSave);
    }
}