package phoenix.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetails;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String lr=new BCryptPasswordEncoder().encode("test");
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/static/**", "/images/**", "/js/**").permitAll()
                .antMatchers("/bdays", "/bdays/**","/add","/editBirthday/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/templates/**", "/addTemplate",
                        "/editTemplate/**", "/users/**", "/addUser", "/editUser/**").hasRole("ADMIN")
                .antMatchers("/404", "/400", "/500").authenticated()
                .antMatchers("/login").anonymous()
                .anyRequest().permitAll()
                .and()

                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/bdays")
                .successForwardUrl("/bdays")
                .failureHandler(failureHandler())
                .permitAll()
                .usernameParameter("userLogin")
                .passwordParameter("password")

                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)

                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);
    }
}
