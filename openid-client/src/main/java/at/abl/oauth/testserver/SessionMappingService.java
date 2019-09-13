package at.abl.oauth.testserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//!!!WARNING!!! memory leak, needs periodic cleanup. also not threadsafe, this will lead to exceptions
@Service
public class SessionMappingService
{
    private final Logger LOG = LoggerFactory.getLogger(SessionMappingService.class);

    private Map<String, HttpSession> sessionMap;

    public SessionMappingService()
    {
        sessionMap = new HashMap<>();
    }

    public void addSession(String sid, HttpSession session)
    {
        LOG.debug("putting new session with sid {} and sessionid {}", sid, session.getId());
        sessionMap.put(sid, session);
    }


    public void invalidateSession(String sid)
    {
        if (sessionMap.containsKey(sid))
        {
            try
            {
                HttpSession session = sessionMap.get(sid);
                String sessionId = session.getId();
                session.invalidate();
                LOG.debug("invalidated session with sid {} and sessionId {}", sid, sessionId);
            }
            catch (Exception e)
            {
                LOG.debug("error invalidating session with sid {}", sid, e);
                //ignored
            }
        }
        else
        {
            LOG.debug("session with sid {} not found", sid);
        }
    }
}
