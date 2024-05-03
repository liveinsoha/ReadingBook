package com.example.demo.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final int ONE_MONTH = 2678400;
    private final MemberDetailsService memberDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        return http
                .csrf(customizer -> customizer
                        .csrfTokenRequestHandler(requestHandler)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/","/account/**", "/logout/**", "/register", "/register/validate/email")
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/cart", "/library", "/user/**").hasAnyRole("MEMBER", "ADMIN", "VENDOR")
                        .requestMatchers(
                                "/manage/**",
                                "/register/author", "/update/author", "/delete/author", "/search/author",
                                "/register/book-author-list", "/delete/book-author-list",
                                "/register/book", "/update/book", "/delete/book", "/search/book", "/update/search/book",
                                "/register/book-content", "/update/book-content", "/delete/book-content", "/update/search/book-content",
                                "/register/book-group", "/update/book-group", "/delete/book-group", "/search/book-group",
                                "/register/category-group", "/update/category-group", "/delete/category-group", "/search/category-group",
                                "/register/category", "/update/category", "/delete/category", "/search/category", "/search/categories"
                        ).hasRole("ADMIN")
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().permitAll()

                )
                .formLogin(form -> form
                        .loginPage("/account/login")
                        .loginProcessingUrl("/account/login")
                        .usernameParameter("email")
                        .successHandler(new LoginSuccessHandler("/"))
                        .failureHandler(new LoginFailHandler())
                        .permitAll()
                ).rememberMe(customizer -> customizer
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(ONE_MONTH)
                        .userDetailsService(memberDetailsService)
                        .authenticationSuccessHandler(new LoginSuccessHandler())
                ).logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("remember-me")
                        .permitAll()
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}