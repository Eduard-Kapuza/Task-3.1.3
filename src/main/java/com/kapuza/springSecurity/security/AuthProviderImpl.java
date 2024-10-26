package com.kapuza.springSecurity.security;//package com.kapuza.springSecurity.security;
//
//import com.kapuza.springSecurity.services.personService.PersonDetailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//

//----------------------------------------------------------------
//ПРОВЕДЕНИЕ АУТЕНТИФИКАЦИИ
//----------------------------------------------------------------



//@Component
//public class AuthProviderImpl implements AuthenticationProvider {
//
//    private final PersonDetailService personDetailService;
//
//    @Autowired
//    public AuthProviderImpl(PersonDetailService personDetailService) {
//        this.personDetailService = personDetailService;
//    }
//



//НА ВХОДЕ В ПАРАМЕТРАХ МЕТОДА ЛОГИН И ПАРОЛЬ, КОТОРЫЙ ВВЕЛИ НА ФОРМЕ ВХОДА - креденшилс
//А ВОЗВРАЩАЕТ УЖЕ ПРИНЦИПАЛ, ОБЪЕКТ С ДАННЫМИ ПОЛЬЗОВАТЕЛЯ(персон детаилс)
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        String useName = authentication.getName();
//
//        UserDetails userDetails = personDetailService.loadUserByUsername(useName);//получаем имя из формы
//
//        String password = authentication.getCredentials().toString();//получаем пароль из формы
//
//        if (!password.equals(userDetails.getPassword())) {
//            throw new BadCredentialsException("Incorrect password");
//        }
//
//        //возвращаем объект Authentication с principle внутри(данные юзера)
//        return new UsernamePasswordAuthenticationToken(useName, password, Collections.emptyList());
//    }
//

//для каких сценариев используется этот класс для аутентификации
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//}
