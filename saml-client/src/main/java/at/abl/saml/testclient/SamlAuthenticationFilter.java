package at.abl.saml.testclient;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlException;
import com.coveo.saml.SamlResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SamlAuthenticationFilter extends AbstractAuthenticationProcessingFilter
{
    private SamlClient samlClient;

    public SamlAuthenticationFilter(SamlClient samlClient)
    {
        super("/samllogin");
        this.samlClient = samlClient;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException
    {
        try
        {
            SamlResponse samlResponse = samlClient.processPostFromIdentityProvider(request);
            return getAuthenticationManager().authenticate(new PreAuthenticatedAuthenticationToken(samlResponse.getNameID(), samlResponse));
        }
        catch (SamlException e)
        {
            throw new AuthenticationServiceException("error reading saml response", e);
        }
    }
}
