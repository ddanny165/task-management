package dev.ddanny165.taskManagement.configs;

import dev.ddanny165.taskManagement.models.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    private static final String ADMIN = UserRole.ADMIN.name();
    private static final String EMPLOYEE = UserRole.EMPLOYEE.name();

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
