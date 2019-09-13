package at.abl.oauth.testserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private OpenIdConnectFilter openIdConnectFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .addFilterAfter(new OAuth2ClientContextFilter(),
                AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(openIdConnectFilter,
                OAuth2ClientContextFilter.class)
            .httpBasic()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/openidlogin"))
            .and()
            .authorizeRequests()
            .antMatchers("/openidlogout").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf()
            .disable()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/");
    }
}