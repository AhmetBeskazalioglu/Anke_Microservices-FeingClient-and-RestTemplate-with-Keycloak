package com.anke.E_Commerce_Web_Anke.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // add support for JDBC... no more hardcoding users
    /*@Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from members where user_id=?");
        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?");
        return jdbcUserDetailsManager;
    }*/


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(HttpMethod.GET, "/api/feign-client/**").hasAnyRole("EMPLOYEE", "CLIENT")
                     //  .requestMatchers(HttpMethod.GET, "/api/feign-client/cart").hasAnyRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/employees/showFormForUpdate/**").hasAnyRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/employees/delete/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showMyLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/access-denied")

                )
        ;


        // use httpBasic authentication
        //http.httpBasic(Customizer.withDefaults());

        // disable cross side request forgery (CSRF)
        // CSRF saldırıları, kullanıcıların tarayıcıları üzerinden yetkisiz işlemler yapılmasına olanak tanır.
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE, etc.
        //http.csrf(AbstractHttpConfigurer::disable);
        // or
        //http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    // InMemoryUserDetailsManager kullanıcı bilgilerini hafızada tutar.
    public InMemoryUserDetailsManager userDetailsService() {

        // UserDetails nesnesi oluşturulur ve kullanıcı bilgileri set edilir.
        UserDetails employee = User.builder()
                .username("employee")
                .password("{noop}123")
                .roles("EMPLOYEE")
                .build();

        UserDetails manager = User.builder()
                .username("manager")
                .password("{noop}123")
                .roles("EMPLOYEE", "MANAGER")
                .build();

        UserDetails ahmet = User.builder()
                .username("ahmet")
                .password("{noop}123")
                .roles("EMPLOYEE", "ADMIN", "MANAGER")
                .build();

        UserDetails client = User.builder()
                .username("client")
                .password("{noop}123")
                .roles("CLIENT")
                .build();

        return new InMemoryUserDetailsManager(employee, manager, ahmet, client);
    }
}
