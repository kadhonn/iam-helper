package at.abl.oauth.testserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableOAuth2Client
public class OpenIdConnectConfiguration
{
    @Value("${openid.clientId}")
    private String clientId;

    @Value("${openid.clientSecret}")
    private String clientSecret;

    @Value("${openid.accessTokenUri}")
    private String accessTokenUri;

    @Value("${openid.userAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${openid.redirectUri}")
    private String redirectUri;

    @Value("${openid.additionalScopes:}")
    private String additionalScopes;


    @Bean
    public OpenIdConnectFilter openIdConnectFilter(OAuth2RestTemplate oAuth2RestTemplate, SessionMappingService sessionMappingService)
    {
        OpenIdConnectFilter filter = new OpenIdConnectFilter("/openidlogin", sessionMappingService);
        filter.setRestTemplate(oAuth2RestTemplate);
        return filter;
    }

    @Bean
    public OAuth2ProtectedResourceDetails openIdConfig()
    {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setScope(getScopes());
        details.setPreEstablishedRedirectUri(redirectUri);
        details.setUseCurrentUri(false);
        return details;
    }

    private List<String> getScopes()
    {
        List<String> scopes = new ArrayList<>();
        scopes.add("openid");
        scopes.addAll(Arrays.stream(additionalScopes.split(",")).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
        return scopes;
    }

    @Bean
    public OAuth2RestTemplate googleOpenIdTemplate(OAuth2ClientContext clientContext)
    {
        return new OAuth2RestTemplate(openIdConfig(), clientContext);
    }
}
