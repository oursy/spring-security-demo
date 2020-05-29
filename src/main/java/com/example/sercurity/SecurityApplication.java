package com.example.sercurity;

import com.example.sercurity.dsl.MultipleAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @RequestMapping(value = "/user")
    public String user(Principal principal) {
        return principal.getName();
    }

    @RequestMapping(value = "/admin")
    public String admin(Principal principal) {
        return principal.getName();
    }

    @Configuration
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeRequests(ae -> ae.mvcMatchers("/admin/login", "/user/login").permitAll().anyRequest().authenticated())
                    .addFilter(new MultipleAuthenticationFilter(adminAuthenticationManager(), "/admin/login"))
                    .addFilter(new MultipleAuthenticationFilter(userAuthenticationManager(), "/user/login"));
        }

        @Bean
        @Primary
        AuthenticationManager adminAuthenticationManager() {
            List<AuthenticationProvider> providers = new ArrayList<>();
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setUserDetailsService(adminUserDetailsService());
            providers.add(daoAuthenticationProvider);
            return new ProviderManager(providers);
        }


        UserDetailsService adminUserDetailsService() {
            UserDetails userAdmin = User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN").build();
            return new InMemoryUserDetailsManager(userAdmin);
        }

        @Bean
        AuthenticationManager userAuthenticationManager() throws Exception {
            List<AuthenticationProvider> providers = new ArrayList<>();
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setUserDetailsService(userUserDetailsService());
            daoAuthenticationProvider.afterPropertiesSet();
            providers.add(daoAuthenticationProvider);
            return new ProviderManager(providers);
        }

        UserDetailsService userUserDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
            return new InMemoryUserDetailsManager(user);
        }
    }


}
