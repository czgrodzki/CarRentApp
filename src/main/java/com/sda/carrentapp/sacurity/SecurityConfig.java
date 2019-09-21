package com.sda.carrentapp.sacurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login-form", "/register").permitAll()
                .antMatchers("/booking/selectDateAndLocation").permitAll()
                .antMatchers("/booking/selectCar").permitAll()
                .antMatchers("/booking/selectCar/**").hasRole("USER")
                .antMatchers("/booking/allBookings").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                .antMatchers("/fleet").permitAll()
                .antMatchers("/branches").permitAll()
                .antMatchers("/userPanel").hasRole("USER")
                .antMatchers("/cars").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                .antMatchers("/employees").hasRole("ADMIN")
                .antMatchers("/departments").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                .and()
                .formLogin().loginPage("/login-form").permitAll() // przekierowanie na widok logowania
                .loginProcessingUrl("/login").permitAll()
                .and().headers().frameOptions().sameOrigin()  // pozwolenie na logowanie do bazy danych 'h2-console'
                .and()
                .logout()
                .logoutSuccessUrl("/");
    }
}
