package at.abl.saml.testclient;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
public class HomeController
{
    @RequestMapping("/")
    @ResponseBody
    public String home()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SamlUser samlUser = (SamlUser) authentication.getPrincipal();
        return "<pre>Welcome, " + samlUser.getUsername() + "\n\nAttributes:\n"
            + samlUser.getAttributes().entrySet().stream().map(entry -> "  " + entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining("\n"))
            + "</pre>" +
            "<a href=\"/logout\">Logout</a>";
    }
}
