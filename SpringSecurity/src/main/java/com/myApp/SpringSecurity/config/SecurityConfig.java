package com.myApp.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*
     * Se define reglas y configuraciones relacionadas con la seguridad.
     * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/index").permitAll();
                    auth.requestMatchers("/session-expired").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin()
                    .successHandler(successHandler()).permitAll()
                .and()
                .sessionManagement() //Administrador de la session
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .invalidSessionUrl("/login") // Redirige a la página de login cuando se detecta una sesión inválida.
                    .maximumSessions(1)
                    .expiredUrl("/session-expired")// Redirige a la página de sesión expirada cuando se detecta una sesión expirada
                    .sessionRegistry(sessionRegistry()) //se utiliza para habilitar el registro de sesiones
                .and()
                .sessionFixation() // Define cómo se manejaN los problemas de seguridad relacionados con la fijación de sesiones.
                    .migrateSession() // .migrateSession() - .newSession()
                .and()
                .build();
    }

    //Registramos los datos de la session
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    //Método para redirigir a otra página
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect("/api/v1/session");
        });
    }
}
