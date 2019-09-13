package at.abl.oauth.testserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class OpenIdConnectFilter extends AbstractAuthenticationProcessingFilter
{

    private static final Logger LOG = LoggerFactory.getLogger(OpenIdConnectFilter.class);

    private OAuth2RestTemplate restTemplate;
    private final SessionMappingService sessionMappingService;

    public OpenIdConnectFilter(String defaultFilterProcessesUrl, SessionMappingService sessionMappingService)
    {
        super(defaultFilterProcessesUrl);
        //nothing to authenticate
        setAuthenticationManager(authentication -> authentication);
        this.sessionMappingService = sessionMappingService;
    }

    public void setRestTemplate(OAuth2RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException
    {
        OAuth2AccessToken accessToken;
        try
        {
            accessToken = restTemplate.getAccessToken();
        }
        catch (OAuth2Exception e)
        {
            LOG.error("error while resolving access token", e);
            throw new BadCredentialsException("Could not obtain access token", e);
        }
        try
        {
            String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
            String kid = JwtHelper.headers(idToken).get("kid");
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(idToken, verifier(kid));
            Map<String, String> authInfo = new ObjectMapper()
                .readValue(tokenDecoded.getClaims(), Map.class);
            verifyClaims(authInfo);
            OpenIdConnectUserDetails user = new OpenIdConnectUserDetails(authInfo, accessToken);
            sessionMappingService.addSession(authInfo.get("sid"), request.getSession());
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        catch (InvalidTokenException e)
        {
            LOG.error("error reading id token", e);
            throw new BadCredentialsException("Could not obtain user details from token", e);
        }
    }

    private SignatureVerifier verifier(String kid)
    {
        return new SignatureVerifier()
        {
            @Override
            public void verify(byte[] content, byte[] signature)
            {
                //!!!WARNING!!! here should be an actual verifier, not my test-noop
            }

            @Override
            public String algorithm()
            {
                return "noop";
            }
        };
    }

    private void verifyClaims(Map<String, String> authInfo)
    {
        //nothing to verify
    }
}
