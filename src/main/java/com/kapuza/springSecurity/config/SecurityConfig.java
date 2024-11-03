package com.kapuza.springSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class SecurityConfig {


    /**
     * Бин для шифровки пароля
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Настройка конфигурации SS
     */
    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> {auth
                                 .requestMatchers("/auth/**", "/registration/**", "/error/**").permitAll()//доступ на ... без аутентификации для всех
                                 .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")//доступ для аутентифицированных и авторизированных под админом
                                 .requestMatchers("/api/v1//users/**").hasRole("USER")//доступ для аутентифицированных и авторизированных под юзером
                                 .anyRequest().authenticated();// доступ ко всем остальным адресам для аутентифицированных
                        }
                ).logout(l -> l.logoutUrl("/logout")//при переходе на этот адрес будут происходить разлогин
                        .logoutSuccessUrl("/auth/login")//при успешном разлогине будет перекидывать на указанный адрес
                        .invalidateHttpSession(true)//удаляем саму сессию
                        .deleteCookies("JSESSIONID"))//удаляем куки
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler(new LoginSuccessHandler()) // для переброса на дефолтную страницу после аутентификации согласно авторизации по роли
                        .permitAll()
                ).build();
    }
}