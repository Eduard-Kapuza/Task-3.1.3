package com.kapuza.springSecurity.services.userService;

import com.kapuza.springSecurity.exceptionsHandling.NotFoundUserException;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.repositories.UserRepository;
import com.kapuza.springSecurity.security.UserDetailsImpl;
import com.kapuza.springSecurity.services.roleService.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleServiceImpl roleServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleServiceImpl roleServiceImpl, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleServiceImpl = roleServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Список ролей прежний т.к. юзер не может сам себе изменить роли
     */
    private User userUpdateData(User userForUpdate, User newDataUser) {
        log.info("\u001B[31mЧЕМУ РАВЕН userForUpdate ПРИ ОБНОВЛЕНИИ ЧЕРЕЗ ЮЗЕРА: " + userForUpdate + " \u001B[0m");
        /**Проверка на изменение имени, пароля, года рождения, если что-то менялось то присваиваем новое значение*/
        userForUpdate = checkNamePasswordYearOfBirthRoles(userForUpdate, newDataUser);
        return userForUpdate;
    }

    private User adminUpdateData(User userForUpdate, User newDataUser) {
        log.info("\u001B[31mДанные до обновления: " + userForUpdate + " \u001B[0m");

        /**Проверка на изменение имени, пароля, года рождения, списка ролей, если что-то менялось то присваиваем новое значение*/
        checkNamePasswordYearOfBirthRoles(userForUpdate, newDataUser);
//        /**Если список ролей редактировалось или не стал пустым, то назначаем новый список ролей*/
//        if (!new HashSet<>(userForUpdate.getRoles()).containsAll(newDataUser.getRoles()) & newDataUser.getRoles() != null) {
//            userForUpdate.setRoles(newDataUser.getRoles());
//        }
        return userForUpdate;

    }

    private User checkNamePasswordYearOfBirthRoles(User userForUpdate, User newDataUser) {
        /**Если имя редактировалось или не стало пустым, то назначаем новое имя*/
        if (newDataUser.getUsername() != null & !newDataUser.getUsername().isBlank() & !(Objects.equals(userForUpdate.getUsername(), newDataUser.getUsername()))) {
            userForUpdate.setUsername(newDataUser.getUsername());
        }

        /**Если год рождения редактировался или не стало пустым, то назначаем новое значение*/
        if (newDataUser.getYearOfBirth() != null & newDataUser.getYearOfBirth() != null & !(Objects.equals(userForUpdate.getYearOfBirth(), newDataUser.getYearOfBirth()))) {
            userForUpdate.setYearOfBirth(newDataUser.getYearOfBirth());
        }

        /**Если пароль редактировался или не стал пустым, то назначаем новое значение*/
        if (newDataUser.getPassword() != null & !newDataUser.getUsername().isBlank() & !(Objects.equals(userForUpdate.getPassword(), passwordEncoder.encode(newDataUser.getPassword())))) {
            /**шифруем пароль, который ввёл в форме юзер*/
            String encodePassword = passwordEncoder.encode(newDataUser.getPassword());
            /**устанавливаем шифрованный пароль юзеру*/
            userForUpdate.setPassword(encodePassword);
        }

        /**Получаем текущего пользователя в системе*/
        User currentPersonInSystem = UserDetailsImpl.getUser();
        /**Эта проверка для того, чтобы текущий пользователь или админ не могли сменить себе роль*/
        log.info("\u001B[31mСписок ролей до обновления списка ролей:  " + userForUpdate.getRoles().toString() + " \u001B[0m");
        if (userForUpdate != currentPersonInSystem & !newDataUser.getRoles().isEmpty() & newDataUser.getRoles() != null) {
            for (Role o : newDataUser.getRoles()) {
                Optional<Role> byRoleName = roleServiceImpl.findByRoleName(o.getRoleName());
                if(byRoleName.isPresent()){
                    log.info("\u001B[31mНайденная роль:  " + byRoleName.get() + " \u001B[0m");
                    userForUpdate.addRole(byRoleName.get());
                }else {
                    log.info("\u001B[31mВы добавляете не существующую роль:  " + byRoleName.get() + " \u001B[0m");
                    throw new RuntimeException("Вы добавляете не существующую роль");
                }
            }
        }

        log.info("\u001B[31mПрошли метод checkNamePasswordYearOfBirthRoles, юзер на выходе метода: " + userForUpdate + " \u001B[0m");
        return userForUpdate;
    }

    @Override
    public User updateDataUser(User newDataUser) {
        Optional<User> userForUpdate = userRepository.findById(newDataUser.getId());
        if (userForUpdate.isEmpty()) {
            throw new NotFoundUserException("Пользователь: " + newDataUser + " не найден для обновления");
        }

        log.info("\u001B[35mНачато обновление данных пользователя: " + userForUpdate.get() + "\u001B[0m");

        /**Получаем текущего пользователя в системе для того что бы знать что он может обновлять*/
        User currentUserInSystem = UserDetailsImpl.getUser();


        /**Если текущий пользователь админ, то ему доступно больше полномочий для редактирования*/
        for (Role role : currentUserInSystem.getRoles()) {
            if (role.getRoleName().equals("ROLE_ADMIN")) {
                log.info("\u001B[33mОбновляет ADMIN: " + currentUserInSystem.getUsername() + " \u001B[0m");
                newDataUser = adminUpdateData(userForUpdate.get(), newDataUser);
            }
            if (role.getRoleName().equals("ROLE_USER")) {
                log.info("\u001B[33mОбновляет USER: " + currentUserInSystem.getUsername() + " \u001B[0m");
                newDataUser = userUpdateData(userForUpdate.get(), newDataUser);
            }
        }
        return userRepository.save(newDataUser);
    }

    @Override
    public User save(User user) {
        log.info("\u001B[33m Юзер: " + user + " сохранён в БД\u001B[0m");
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUserName(User user) {
        return userRepository.findByUsername(user.getUsername());
    }

    @Override
    public User delete(Long id) {
        Optional<User> byId = findById(id);
        byId.ifPresent(userRepository::delete);
        if (byId.isEmpty()) {
            throw new NotFoundUserException("Used for delete not found");
        }
        log.info("\u001B[33m Удаляемый юзер: " + byId.get() + "\u001B[0m");

        return byId.get();
    }


}