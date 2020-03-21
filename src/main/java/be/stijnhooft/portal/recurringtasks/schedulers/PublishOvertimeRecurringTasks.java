package be.stijnhooft.portal.recurringtasks.schedulers;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import be.stijnhooft.portal.recurringtasks.mappers.event.CancellationEventMapper;
import be.stijnhooft.portal.recurringtasks.mappers.event.ReminderEventMapper;
import be.stijnhooft.portal.recurringtasks.messaging.EventPublisher;
import be.stijnhooft.portal.recurringtasks.services.RecurringTaskService;
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

        Set<RecurringTaskDto> overtimeTasks = findRecurringTasksThatTurnOvertime();
        Set<RecurringTaskDto> seriouslyOvertimeTasks = findRecurringTasksThatTurnSeriouslyOvertime();

        cancelEarlierPublishedOvertimeEventsThatNowBecomeSeriouslyOvertimeEvents(seriouslyOvertimeTasks);
        publishAsEvents(overtimeTasks, seriouslyOvertimeTasks);
    }

    private void cancelEarlierPublishedOvertimeEventsThatNowBecomeSeriouslyOvertimeEvents(Set<RecurringTaskDto> seriouslyOvertimeTasks) {
        final Set<Event> cancellationEvents = seriouslyOvertimeTasks.stream()
                .map(cancellationEventMapper::map)
                .collect(Collectors.toSet());

        if (!cancellationEvents.isEmpty()) {
            eventPublisher.publish(cancellationEvents);
        }
    }

    private void publishAsEvents(Set<RecurringTaskDto> overtimeEvents, Set<RecurringTaskDto> seriouslyOvertimeEvents) {
        Set<Event> allEvents = Stream   .concat(overtimeEvents.stream(), seriouslyOvertimeEvents.stream())
                                        .map(reminderEventMapper::map)
                                        .collect(Collectors.toSet());
        if (!allEvents.isEmpty()) {
            eventPublisher.publish(allEvents);
        }
    }

    private Set<RecurringTaskDto> findRecurringTasksThatTurnOvertime() {
        return recurringTaskService.findAll()
                .stream()
                .filter(this::isTodayEqualToMinDueDate)
                .collect(Collectors.toSet());
    }

    private Set<RecurringTaskDto> findRecurringTasksThatTurnSeriouslyOvertime() {
        return recurringTaskService.findAll()
                .stream()
                .filter(this::isMaxNumberOfDaysGreaterThanMinNumberOfDaysBetweenExecutions) // tasks with min = max cannot turn seriously overtime
                .filter(this::isTodayEqualToMaxDueDate)
                .collect(Collectors.toSet());
    }

    private boolean isMaxNumberOfDaysGreaterThanMinNumberOfDaysBetweenExecutions(RecurringTaskDto recurringTaskDto) {
        return recurringTaskDto.getMaxNumberOfDaysBetweenExecutions() > recurringTaskDto.getMinNumberOfDaysBetweenExecutions();
    }

    private boolean isTodayEqualToMinDueDate(RecurringTaskDto task) {
        LocalDate dueDate = LocalDate.now().minusDays(task.getMinNumberOfDaysBetweenExecutions());
        return task.getLastExecution() != null && task.getLastExecution().isEqual(dueDate);
    }

    private boolean isTodayEqualToMaxDueDate(RecurringTaskDto task) {
        LocalDate dueDate = LocalDate.now().minusDays(task.getMaxNumberOfDaysBetweenExecutions());
        return task.getLastExecution() != null && task.getLastExecution().isEqual(dueDate);
    }
}
