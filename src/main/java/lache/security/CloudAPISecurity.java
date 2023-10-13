package lache.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class CloudAPISecurity {

    private DataSource dataSource;

    public CloudAPISecurity(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        //configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        //configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Access-Control-Allow-Credentials", "Access-Control-Allow-Origin", "Origin","Referer", "Accept", "Content-Type",
                "Access-Control-Allow-Methods", "Access-Control-Allow-Headers", "Host","Authorization",  "X-Requested-With"));
        //configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/logout").permitAll()
                        //.requestMatchers(OPTIONS).permitAll()
                        .requestMatchers("/cloud/").hasRole("USER")
                        .anyRequest().authenticated())
                .formLogin(
                        //(form) -> form.permitAll())
                        form -> form
                                .loginPage("http://localhost:8080/login")
                                .permitAll())
                .logout((logout) -> logout.permitAll())
                //.cors(withDefaults())
        ;
        return http.build();
    }


    @Bean
    public UserDetailsManager user(DataSource dataSource) {
        JdbcUserDetailsManager user = new JdbcUserDetailsManager(dataSource);
        user.setUsersByUsernameQuery("SELECT login AS username, password, 'true' AS enabled FROM cloud.users WHERE login=?");
        user.setGroupAuthoritiesSql("SELECT login AS username, role FROM cloud.users WHERE login=?");
        return user;
    }

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT login AS username, password, 1 AS enabled FROM cloud.users WHERE login=?")
                .authoritiesByUsernameQuery("SELECT login AS username, role FROM cloud.users WHERE login=?");
    }*/

}
