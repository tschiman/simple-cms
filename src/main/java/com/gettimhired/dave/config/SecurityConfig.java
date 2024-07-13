package com.gettimhired.dave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    @Profile("!local")
    public SecurityFilterChain filterChainForm(HttpSecurity http) throws Exception {
        return http
                .requiresChannel(channel ->
                        channel.anyRequest().requiresSecure())
                .formLogin(formLogin -> {
                    formLogin.defaultSuccessUrl("/account");
                })
                .logout(logout -> {
                    logout.logoutSuccessUrl("/");
                })
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/admin").authenticated();
                    authorize.anyRequest().permitAll();
                })
                .userDetailsService(inMemoryUserDetailsService())
                .build();
    }

    @Bean
    @Order(1)
    @Profile("local")
    public SecurityFilterChain filterChainLocalForm(HttpSecurity http) throws Exception {
        return http
                .formLogin(formLogin -> {
                    formLogin.defaultSuccessUrl("/admin");
                    formLogin.loginPage("/login");
                })
                .logout(logout -> {
                    logout.logoutSuccessUrl("/");
                })
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/admin").authenticated();
                    authorize.requestMatchers("/job/new").authenticated();
                    authorize.anyRequest().permitAll();
                })
                .userDetailsService(inMemoryUserDetailsService())
                .build();
    }

    @Bean
    UserDetailsService inMemoryUserDetailsService() {
        var imuds = new InMemoryUserDetailsManager();
        imuds.createUser(User.withUsername("test@example.com")
                .password("{noop}password") // {noop} is used to specify that no password encoder is used
                .roles("USER")
                .build());
        return imuds;
    }
}
