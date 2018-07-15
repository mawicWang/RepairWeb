package com.duofuen.repair.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Autowired
    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        // ensure the passwords are encoded properly
////        User.UserBuilder users = User.withDefaultPasswordEncoder();
//        auth
//                .jdbcAuthentication()
//                .dataSource(dataSource);
////                .withDefaultSchema();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
//        .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
//                .withUser("aa").password(passwordEncoder().encode("aa")).roles("AA")
//                .and().withUser("bb").password(passwordEncoder().encode("bb")).roles("B")
//                .and().withUser("qq").password(passwordEncoder().encode("qq")).roles("Q")
//                .and().withUser("wang").password(passwordEncoder().encode("123456")).roles("USER");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/js/**", "/index", "/", "/rest/**", "/wx/**").permitAll()
//                .antMatchers("/home").hasRole("USER")
                .antMatchers("/listOrder", "/changeRepairman", "/saveChangeRepairman",
                        "/changePassword", "/saveChangePassword", "/showOrderHistory", "/home", "/welcome").hasAnyRole("USER","ADMIN")
                .antMatchers("/**").hasRole("ADMIN")
                .and()
            .formLogin()
                .loginPage("/login").defaultSuccessUrl("/home").permitAll()
                .and()
            .logout().permitAll()
                .and()
            .csrf().ignoringAntMatchers("/rest/**");
        super.configure(http);
    }

}
