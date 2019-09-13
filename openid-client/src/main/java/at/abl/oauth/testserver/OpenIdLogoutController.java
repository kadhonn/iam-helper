package at.abl.oauth.testserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class OpenIdLogoutController
{
    private static final Logger LOG = LoggerFactory.getLogger(OpenIdLogoutController.class);

    private final SessionMappingService sessionMappingService;

    @Autowired
    public OpenIdLogoutController(SessionMappingService sessionMappingService)
    {
        this.sessionMappingService = sessionMappingService;
    }

    @PostMapping(value = "/openidlogout", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> logout(@RequestParam("logout_token") String logoutToken)
    {
        try
        {
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(logoutToken, verifier());
            Map<String, String> authInfo = new ObjectMapper()
                .readValue(tokenDecoded.getClaims(), Map.class);
            System.out.println(authInfo.toString());
            sessionMappingService.invalidateSession(authInfo.get("sid"));
        }
        catch (InvalidTokenException | IOException e)
        {
            LOG.error("error reading logout token", e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    private SignatureVerifier verifier()
    {
        return new SignatureVerifier()
        {
            @Override
            public void verify(byte[] content, byte[] signature)
            {
                //!!!WARNING!!! here should be an actual verifier
            }

            @Override
            public String algorithm()
            {
                return "noop";
            }
        };
    }
}
