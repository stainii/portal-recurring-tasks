package be.stijnhooft.portal.housagotchi.schedulers;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.EventMapper;
import be.stijnhooft.portal.housagotchi.messaging.EventPublisher;
import be.stijnhooft.portal.housagotchi.services.RecurringTaskService;
import be.stijnhooft.portal.model.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@EnableScheduling
public class PublishOvertimeRecurringTasks {

    private final RecurringTaskService recurringTaskService;
    private final EventMapper eventMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public PublishOvertimeRecurringTasks(RecurringTaskService recurringTaskService, EventMapper eventMapper, EventPublisher eventPublisher) {
        this.recurringTaskService = recurringTaskService;
        this.eventMapper = eventMapper;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(cron = "0 0 16 * * *")
    public void publishOvertimeRecurringTasks() {
        log.info("Checking if overtime recurring tasks need to be published");

        Set<Event> events = mapRecurringTasksThatTurnOvertimeToEvents();
        events.addAll(mapRecurringTasksThatTurnSeriouslyOvertimeToEvents());

        if (!events.isEmpty()) {
            eventPublisher.publish(events);
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
        return task.getLastExecution() != null && task.getLastExecution().isEqual(dueDate);
    }

    private boolean isTodayEqualToMaxDueDate(RecurringTaskDTO task) {
        LocalDate dueDate = LocalDate.now().minusDays(task.getMaxNumberOfDaysBetweenExecutions());
        return task.getLastExecution() != null && task.getLastExecution().isEqual(dueDate);
    }
}
