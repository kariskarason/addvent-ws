package is.nord.config;

import is.nord.FlashMessage;
import is.nord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                //.antMatchers("/**").permitAll()
                //.antMatchers("/about", "/news/{newsId}", "/event/{newsId}", "/ad/{adId}.jpg").permitAll()
                .antMatchers("/ad/{adId}.jpg", "/infoBoard/{infoBoardId}.jpg", "/news/{newsId}.jpeg").permitAll()
                .antMatchers("/user/userList").access("hasRole('ADMIN') or hasRole('USER')")
                .antMatchers("/chooseBus").access("hasRole('ADMIN') or hasRole('USER')")
                .antMatchers("/register").access("hasRole('ADMIN') or hasRole('USER')")
                .antMatchers("/unregister").access("hasRole('ADMIN') or hasRole('USER')")
                .antMatchers("/user/add").hasRole("ADMIN")
                .antMatchers("/user/save").hasRole("ADMIN")
                .antMatchers("/user/{userId}/eventBan").hasRole("ADMIN")
                .antMatchers("/user/{userId}/removeEventBan").hasRole("ADMIN")
                .antMatchers("/ad/add").hasRole("ADMIN")
                .antMatchers("/ad/{adId}/edit").hasRole("ADMIN")
                .antMatchers("/ad/save").hasRole("ADMIN")
                .antMatchers("/ad/{adId}").hasRole("ADMIN")
                .antMatchers("/ad/{adId}/delete").hasRole("ADMIN")
                .antMatchers("/infoNord/add").hasRole("ADMIN")
                .antMatchers("/infoNord/{infoId}/edit").hasRole("ADMIN")
                .antMatchers("/infoNord/save").hasRole("ADMIN")
                .antMatchers("/infoNord/{infoId}").hasRole("ADMIN")
                .antMatchers("/infoNord/{infoId}/delete").hasRole("ADMIN")
                .antMatchers("/infoBoard/add").hasRole("ADMIN")
                .antMatchers("/infoBoard/{infoId}/edit").hasRole("ADMIN")
                .antMatchers("/infoBoard/save").hasRole("ADMIN")
                .antMatchers("/infoBoard/{infoId}").hasRole("ADMIN")
                .antMatchers("/infoBoard/{infoId}/delete").hasRole("ADMIN")
                .antMatchers("/news/add").hasRole("ADMIN")
                .antMatchers("/news/{newsId}/edit").hasRole("ADMIN")
                .antMatchers("/news/save").hasRole("ADMIN")
                .antMatchers("/news/{newsId}/delete").hasRole("ADMIN")
                .antMatchers("/event/add").hasRole("ADMIN")
                .antMatchers("/event/{newsId}/edit").hasRole("ADMIN")
                .antMatchers("/event/save").hasRole("ADMIN")
                .antMatchers("/event/{newsId}/delete").hasRole("ADMIN")
                //.anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                //.successHandler(loginSuccessHandler())
                //.failureHandler(loginFailureHandler())
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/")
                .and()
            .csrf();
    }

    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> response.sendRedirect("/");
    }

    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().setAttribute("flash", new FlashMessage("Rangt notandanafn eða lykilorð. Reyndu aftur.", FlashMessage.Status.FAILURE));
            response.sendRedirect("/login");
        };
    }
}
