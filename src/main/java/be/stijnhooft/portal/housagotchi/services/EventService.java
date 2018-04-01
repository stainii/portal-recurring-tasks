package be.stijnhooft.portal.housagotchi.services;

import be.stijnhooft.portal.model.domain.Event;

import java.util.Collection;

/** Implementation: see {@link be.stijnhooft.portal.housagotchi.ModuleConfiguration#createEventService(org.apache.camel.CamelContext)} **/
public interface EventService {

    void publishEvents(Collection<Event> event);

}
