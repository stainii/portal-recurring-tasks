package be.stijnhooft.portal.housagotchi.schedulers;

import be.stijnhooft.portal.housagotchi.PortalHousagotchi;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.EventMapper;
import be.stijnhooft.portal.housagotchi.messaging.EventPublisher;
import be.stijnhooft.portal.housagotchi.services.RecurringTaskService;
import be.stijnhooft.portal.model.domain.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PublishOvertimeRecurringTasksTest {

    @InjectMocks
    private PublishOvertimeRecurringTasks publishOvertimeRecurringTasks;

    @Mock
    private RecurringTaskService recurringTaskService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventPublisher eventPublisher;


    @Test
    public void publishOvertimeRecurringTasks() {
        //data set
        Event event1 = new Event(PortalHousagotchi.APPLICATION_NAME, LocalDateTime.now(), new HashMap<>());
        Event event2 = new Event(PortalHousagotchi.APPLICATION_NAME, LocalDateTime.now(), new HashMap<>());

        RecurringTaskDTO recurringTask1 = new RecurringTaskDTO(1L, "1", 1, 2, LocalDate.now());
        RecurringTaskDTO recurringTask2 = new RecurringTaskDTO(2L, "2", 2, 3, LocalDate.now().minusDays(2));
        RecurringTaskDTO recurringTask3 = new RecurringTaskDTO(3L, "3", 3, 4, LocalDate.now().minusDays(1));
        RecurringTaskDTO recurringTask4 = new RecurringTaskDTO(4L, "4", 4, 5, LocalDate.now().minusDays(5));

        //mock
        doReturn(Arrays.asList(recurringTask1, recurringTask2, recurringTask3, recurringTask4)).when(recurringTaskService).findAll();
        doReturn(event1).when(eventMapper).map(recurringTask2);
        doReturn(event2).when(eventMapper).map(recurringTask4);

        //execute
        publishOvertimeRecurringTasks.publishOvertimeRecurringTasks();

        //verify
        verify(recurringTaskService, times(2)).findAll();
        verify(eventMapper).map(recurringTask2);
        verify(eventMapper).map(recurringTask4);
        verify(eventPublisher).publish(Stream.of(event1, event2).collect(Collectors.toSet()));
        verifyNoMoreInteractions(recurringTaskService, eventMapper, eventPublisher);
    }

    @Test
    public void publishOvertimeRecurringTasksWhenThereIsNothingToPublish() {
        //data set
        RecurringTaskDTO recurringTask1 = new RecurringTaskDTO(1L, "1", 1, 2, LocalDate.now());
        RecurringTaskDTO recurringTask2 = new RecurringTaskDTO(2L, "2", 2, 3, LocalDate.now().minusDays(4));
        RecurringTaskDTO recurringTask3 = new RecurringTaskDTO(3L, "3", 3, 4, LocalDate.now().minusDays(1));
        RecurringTaskDTO recurringTask4 = new RecurringTaskDTO(4L, "4", 4, 5, LocalDate.now().minusDays(8));

        //mock
        doReturn(Arrays.asList(recurringTask1, recurringTask2, recurringTask3, recurringTask4)).when(recurringTaskService).findAll();

        //execute
        publishOvertimeRecurringTasks.publishOvertimeRecurringTasks();

        //verify
        verify(recurringTaskService, times(2)).findAll();
        verifyNoMoreInteractions(recurringTaskService, eventMapper, eventPublisher);
    }
}