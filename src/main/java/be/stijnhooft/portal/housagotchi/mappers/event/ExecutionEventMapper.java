package be.stijnhooft.portal.housagotchi.mappers.event;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.Mapper;
import be.stijnhooft.portal.model.domain.Event;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static be.stijnhooft.portal.housagotchi.PortalHousagotchi.APPLICATION_NAME;

@Component
public class ExecutionEventMapper extends Mapper<RecurringTaskDTO, Event> {

    @Override
    public Event map(@NonNull RecurringTaskDTO recurringTask) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "execution");
        return new Event(APPLICATION_NAME, APPLICATION_NAME + "-" + recurringTask.getId(), LocalDateTime.now(), data);
    }
}
