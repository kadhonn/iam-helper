package at.abl.saml.testclient;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SamlAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    private SamlClient samlClient;

    public SamlAuthenticationEntryPoint(SamlClient samlClient)
    {
        this.samlClient = samlClient;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException
    {
        try
        {
            samlClient.redirectToIdentityProvider(response, null);
        }
        catch (SamlException e)
        {
            throw new RuntimeException("error creating saml request", e);
        }
    }
}
