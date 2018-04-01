package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.PortalHousagotchiApplication;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.model.domain.Event;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventMapper extends Mapper<RecurringTaskDTO, Event> {

    @Override
    public Event map(@NonNull RecurringTaskDTO recurringTask) {
        LocalDate lastExecution = recurringTask.getLastExecution();
        LocalDate lastAcceptableDayOfExecution = LocalDate.now().minusDays(recurringTask.getMaxNumberOfDaysBetweenExecutions());

        Map<String, String> data = new HashMap<>();
        data.put("urgent", Boolean.toString(lastExecution.isEqual(lastAcceptableDayOfExecution)));
        data.put("task", recurringTask.getName());
        data.put("lastExecution", recurringTask.getLastExecution().toString());

        return new Event(PortalHousagotchiApplication.APPLICATION_NAME, LocalDateTime.now(), data);
    }
}
