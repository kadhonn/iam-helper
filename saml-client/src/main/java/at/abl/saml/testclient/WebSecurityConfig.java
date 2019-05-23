package at.abl.saml.testclient;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    SamlClient samlClient;

    @Bean
    SamlAuthenticationFilter samlAuthenticationFilter() throws Exception
    {
        SamlAuthenticationFilter filter = new SamlAuthenticationFilter(samlClient);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    SamlAuthenticationEntryPoint authenticationEntryPoint()
    {
        return new SamlAuthenticationEntryPoint(samlClient);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager()
    {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(token -> new SamlUser((String) token.getPrincipal(),
            (SamlResponse) token.getCredentials()));
        return new ProviderManager(Collections.singletonList(provider));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .addFilterBefore(samlAuthenticationFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .csrf()
            .disable()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint());

    }
}