package net.chrissearle.spring.twitter.spring;

import net.chrissearle.spring.twitter.service.TimelineService;
import net.chrissearle.spring.twitter.service.TwitterServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service("timelineService")
public class Twitter4jTimelineService extends AbstractTwitter4JSupport implements TimelineService {
    private final Logger logger = Logger.getLogger(Twitter4jTimelineService.class.getName());

    @Autowired
    public Twitter4jTimelineService(Twitter twitter) {
        super(twitter);
    }


    @Override
    public String getLastStatus() {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(new StringBuilder().append("Getting last tweet").toString());
        }

        if (isActive()) {
            return retrieveLastPostFromTimeline();
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Twitter disabled");
            }

            return "Twitter disabled";
        }
    }

    private String retrieveLastPostFromTimeline() {
        try {
            ResponseList<Status> statuslist = retrieveTimeline();

            Status status = statuslist.get(0);

            return status.getText();
        } catch (TwitterException e) {
            final String message = new StringBuilder().append("Unable to get timeline due to ").append(e.getMessage()).toString();

            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }

            throw new TwitterServiceException(message, e);
        }
    }

    private ResponseList<Status> retrieveTimeline() throws TwitterException {
        return twitter.getUserTimeline();
    }

}
