package com.kapuza.springSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/auth/**", "/registration/**", "/error/**").permitAll()//доступ на ... без аутентификации для всех
                                    .requestMatchers("/admin/**").hasRole("ADMIN")//аутентифицирован и авторизирован под админом
                                    .requestMatchers("/persons/**").hasRole("USER")//аутентифицирован и авторизирован под юзером
                                    .anyRequest().authenticated()/*.hasAnyRole("USER", "ADMIN")*/;// доступ ко всему остальному для аутентифицированных и авторизированных под указанными ролями
                        }
                ).formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler(new LoginSuccessHandler())
                        .permitAll()
                ).logout(l -> l.logoutSuccessUrl("/admin/persons/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .build();
    }
}