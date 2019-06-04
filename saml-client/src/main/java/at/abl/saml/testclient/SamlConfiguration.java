package at.abl.saml.testclient;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
public class SamlConfiguration
{

    @Value("${saml.assertionConsumerUrl}")
    String assertionConsumerUrl;

    @Value("${saml.spEntityId}")
    String spEntityId;

    @Bean
    SamlClient samlClient() throws SamlException, FileNotFoundException
    {
        InputStream metadataStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("idp.xml");
        if (metadataStream == null)
        {
            throw new FileNotFoundException("idp.xml was not found on classpath");
        }
        return SamlClient.fromMetadata(spEntityId, assertionConsumerUrl, new InputStreamReader(metadataStream));
    }
}
