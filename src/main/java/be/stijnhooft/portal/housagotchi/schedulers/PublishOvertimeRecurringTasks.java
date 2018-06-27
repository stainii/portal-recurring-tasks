package be.stijnhooft.portal.housagotchi.schedulers;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.EventMapper;
import be.stijnhooft.portal.housagotchi.services.EventService;
import be.stijnhooft.portal.housagotchi.services.RecurringTaskService;
import be.stijnhooft.portal.model.domain.Event;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PublishOvertimeRecurringTasks {

    //one day in milliseconds
    private final static long ONE_DAY = 1 * 24 * 60 * 60 * 1000;

    private final RecurringTaskService recurringTaskService;
    private final EventMapper eventMapper;
    private final EventService eventService;

    @Inject
    public PublishOvertimeRecurringTasks(RecurringTaskService recurringTaskService, EventMapper eventMapper, EventService eventService) {
        this.recurringTaskService = recurringTaskService;
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @Scheduled(fixedRate = ONE_DAY, initialDelay = 10000)
    public void publishOvertimeRecurringTasks() {
        Set<Event> events = mapRecurringTasksThatTurnOvertimeToEvents();
        events.addAll(mapRecurringTasksThatTurnSeriouslyOvertimeToEvents());

        if (!events.isEmpty()) {
            eventService.publishEvents(events);
        }
    }

    private Set<Event> mapRecurringTasksThatTurnOvertimeToEvents() {
        return recurringTaskService.findAll()
                .stream()
                .filter(this::isTodayEqualToMinDueDate)
                .map(eventMapper::map)
                .collect(Collectors.toSet());
    }

    private Set<Event> mapRecurringTasksThatTurnSeriouslyOvertimeToEvents() {
        return recurringTaskService.findAll()
                .stream()
                .filter(this::isTodayEqualToMaxDueDate)
                .map(eventMapper::map)
                .collect(Collectors.toSet());
    }

    private boolean isTodayEqualToMinDueDate(RecurringTaskDTO task) {
        LocalDate dueDate = LocalDate.now().minusDays(task.getMinNumberOfDaysBetweenExecutions());
        return task.getLastExecution().isEqual(dueDate);
    }

    private boolean isTodayEqualToMaxDueDate(RecurringTaskDTO task) {
        LocalDate dueDate = LocalDate.now().minusDays(task.getMaxNumberOfDaysBetweenExecutions());
        return task.getLastExecution().isEqual(dueDate);
    }
}
