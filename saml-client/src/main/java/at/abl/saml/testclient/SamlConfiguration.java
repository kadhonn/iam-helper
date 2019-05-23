package at.abl.saml.testclient;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
public class SamlConfiguration
{
    @Bean
    SamlClient samlClient() throws SamlException, FileNotFoundException
    {
        InputStream metadataStream = ClassLoader.getSystemResourceAsStream("idp.xml");
        if (metadataStream == null)
        {
            throw new FileNotFoundException("idp.xml was not found on classpath");
        }
        return SamlClient.fromMetadata("samlclient", "http://localhost:8081/samllogin",
            new InputStreamReader(metadataStream));
    }
}
