package be.stijnhooft.portal.housagotchi.routes;

import be.stijnhooft.portal.model.domain.Event;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class EventRoute extends RouteBuilder {

    @Override
    public void configure() {
        //TODO: make this sharable config
        JacksonDataFormat formatForListOfEvents = new ListJacksonDataFormat(Event.class);
        formatForListOfEvents.addModule(new ParameterNamesModule());
        formatForListOfEvents.addModule(new Jdk8Module());
        formatForListOfEvents.addModule(new JavaTimeModule());
        formatForListOfEvents.disableFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        from("direct:eventService")
                .marshal(formatForListOfEvents)
                .setExchangePattern(ExchangePattern.InOnly)
                .to("activemq:topic:EventTopic?timeToLive=3600000");
    }

}
