package at.abl.oauth.testserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController
{
    @RequestMapping("/")
    @ResponseBody
    public String home() throws JsonProcessingException
    {
        OpenIdConnectUserDetails userDetails =
            (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "<pre>Welcome, " + userDetails.getUsername() + "\n\n"
            + new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(userDetails.getUserInfo())
            + "</pre>" +
            "<a href=\"/logout\">Logout</a>";
    }
}