package be.stijnhooft.portal.recurringtasks;

import be.stijnhooft.portal.recurringtasks.messaging.EventTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(EventTopic.class)
public class PortalRecurringTasks {

    /**
     * When running this application, don't forget to override the necessary Spring properties.
     * Check out the README for more information.
     */
    public static void main(String[] args) {
        SpringApplication.run(PortalRecurringTasks.class, args);
    }

}
