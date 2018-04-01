package be.stijnhooft.portal.housagotchi.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class EventRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:eventService")
                .marshal()
                .json(JsonLibrary.Jackson)
                .to("log:sending-out-event?level=INFO")
                .to("activemq:topic:EventTopic?timeToLive=3600000");
    }

}
