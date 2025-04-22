package com.challenge.encomendas.encomendas.infrastructure.config;

import com.challenge.encomendas.encomendas.adapters.gateways.FuncionarioGateway;
import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import com.challenge.encomendas.encomendas.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/h2-console/**",
                                        "/api/funcionarios/cadastro",
                                        "/api/funcionarios/login"
                                ).permitAll()
                                .requestMatchers("/api/funcionarios/buscarPorEmail").authenticated()
                                .requestMatchers("/{id}").authenticated()
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public CommandLineRunner init(FuncionarioGateway funcionarioGateway, PasswordEncoder passwordEncoder) {
        return args -> {
            if (funcionarioGateway.findByEmail("teste@teste.com").isEmpty()) {
                Funcionario f = new Funcionario();
                f.setEmail("teste@teste.com");
                f.setNome("Funcionário Teste");
                f.setSenha(passwordEncoder.encode("123456"));
                funcionarioGateway.save(f);
                System.out.println("Funcionário de teste criado com sucesso.");
            }
        };
    }
}
