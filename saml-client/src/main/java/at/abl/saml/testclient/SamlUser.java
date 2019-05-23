package at.abl.saml.testclient;

import com.coveo.saml.SamlResponse;
import org.opensaml.core.xml.schema.XSString;
import org.opensaml.saml.saml2.core.Attribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class SamlUser implements UserDetails
{

    private String username;
    private SamlResponse response;

    public SamlUser(String username, SamlResponse response)
    {
        this.username = username;
        this.response = response;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.emptyList();
    }

    @Override
    public String getPassword()
    {
        return null;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public SamlResponse getResponse()
    {
        return response;
    }

    public Map<String, String> getAttributes()
    {
        return response.getAssertion().getAttributeStatements().stream()
            .flatMap(attributeStatement -> attributeStatement.getAttributes().stream())
            .collect(Collectors.toMap(
                Attribute::getName,
                attribute -> attribute.getAttributeValues().stream()
                    .map(att -> ((XSString) att).getValue())
                    .collect(Collectors.joining(", "))
            ));
    }
}
