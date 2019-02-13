package be.stijnhooft.portal.housagotchi.schedulers;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.event.CancellationEventMapper;
import be.stijnhooft.portal.housagotchi.mappers.event.ReminderEventMapper;
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
import java.util.stream.Stream;

@Component
@Slf4j
@EnableScheduling
public class PublishOvertimeRecurringTasks {

    private final RecurringTaskService recurringTaskService;
    private final ReminderEventMapper reminderEventMapper;
    private final CancellationEventMapper cancellationEventMapper;
    private final EventPublisher eventPublisher;


    @Autowired
    public PublishOvertimeRecurringTasks(RecurringTaskService recurringTaskService,
                                         ReminderEventMapper reminderEventMapper,
                                         CancellationEventMapper cancellationEventMapper,
                                         EventPublisher eventPublisher) {
        this.recurringTaskService = recurringTaskService;
        this.reminderEventMapper = reminderEventMapper;
        this.eventPublisher = eventPublisher;
        this.cancellationEventMapper = cancellationEventMapper;
    }

    @Scheduled(cron = "0 0 16 * * *")
    public void publishOvertimeRecurringTasks() {
        log.info("Checking if overtime recurring tasks need to be published");

        Set<RecurringTaskDTO> overtimeTasks = findRecurringTasksThatTurnOvertime();
        Set<RecurringTaskDTO> seriouslyOvertimeTasks = findRecurringTasksThatTurnSeriouslyOvertime();

        cancelEarlierPublishedOvertimeEventsThatNowBecomeSeriouslyOvertimeEvents(seriouslyOvertimeTasks);
        publishAsEvents(overtimeTasks, seriouslyOvertimeTasks);
    }

    private void cancelEarlierPublishedOvertimeEventsThatNowBecomeSeriouslyOvertimeEvents(Set<RecurringTaskDTO> seriouslyOvertimeTasks) {
        final Set<Event> cancellationEvents = seriouslyOvertimeTasks.stream()
                .map(cancellationEventMapper::map)
                .collect(Collectors.toSet());

        if (!seriouslyOvertimeTasks.isEmpty()) {
            eventPublisher.publish(cancellationEvents);
        }
    }

    private void publishAsEvents(Set<RecurringTaskDTO> overtimeEvents, Set<RecurringTaskDTO> seriouslyOvertimeEvents) {
        Set<Event> allEvents = Stream   .concat(overtimeEvents.stream(), seriouslyOvertimeEvents.stream())
                                        .map(reminderEventMapper::map)
                                        .collect(Collectors.toSet());
        if (!allEvents.isEmpty()) {
            eventPublisher.publish(allEvents);
        }
    }

    private Set<RecurringTaskDTO> findRecurringTasksThatTurnOvertime() {
        return recurringTaskService.findAll()
                .stream()
                .filter(this::isTodayEqualToMinDueDate)
                .collect(Collectors.toSet());
    }

    private Set<RecurringTaskDTO> findRecurringTasksThatTurnSeriouslyOvertime() {
        return recurringTaskService.findAll()
                .stream()
                .filter(this::isTodayEqualToMaxDueDate)
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
