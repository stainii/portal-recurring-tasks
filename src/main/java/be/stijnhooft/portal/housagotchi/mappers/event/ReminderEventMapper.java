package be.stijnhooft.portal.housagotchi.mappers.event;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.Mapper;
import be.stijnhooft.portal.model.domain.Event;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static be.stijnhooft.portal.housagotchi.PortalHousagotchi.APPLICATION_NAME;

@Component
public class ReminderEventMapper extends Mapper<RecurringTaskDTO, Event> {

    @Override
    public Event map(@NonNull RecurringTaskDTO recurringTask) {
        LocalDate lastExecution = recurringTask.getLastExecution();
        LocalDate lastAcceptableDayOfExecution = LocalDate.now().minusDays(recurringTask.getMaxNumberOfDaysBetweenExecutions());

        Map<String, String> data = new HashMap<>();
        data.put("type", "reminder");
        data.put("urgent", Boolean.toString(lastExecution.isEqual(lastAcceptableDayOfExecution)));
        data.put("task", recurringTask.getName());
        data.put("lastExecution", recurringTask.getLastExecution().toString());

        return new Event(APPLICATION_NAME, APPLICATION_NAME + "-" + recurringTask.getId(), LocalDateTime.now(), data);
    }
}
