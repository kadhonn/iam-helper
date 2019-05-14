package at.abl.oauth.testserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenIdTestClientApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OpenIdTestClientApplication.class, args);
    }
}
