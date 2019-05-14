package at.abl.oauth.testserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class OauthConfiguration extends AuthorizationServerConfigurerAdapter
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(
        AuthorizationServerSecurityConfigurer oauthServer)
    {
        oauthServer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
        throws Exception
    {
        clients.inMemory()
            .withClient("acme")
            .secret(passwordEncoder.encode("acmesecret"))
            .autoApprove(".*")
            .scopes("openid", "read", "write")
            .authorizedGrantTypes("authorization_code");
    }

    @Override
    public void configure(
        AuthorizationServerEndpointsConfigurer endpoints)
    {
        endpoints.tokenStore(new InMemoryTokenStore())
            .authenticationManager(authenticationManager);
    }
}
