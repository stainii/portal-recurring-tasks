package be.stijnhooft.portal.housagotchi.schedulers;

import be.stijnhooft.portal.housagotchi.PortalHousagotchi;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.event.CancellationEventMapper;
import be.stijnhooft.portal.housagotchi.mappers.event.ReminderEventMapper;
import be.stijnhooft.portal.housagotchi.messaging.EventPublisher;
import be.stijnhooft.portal.housagotchi.services.RecurringTaskService;
import be.stijnhooft.portal.model.domain.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PublishOvertimeRecurringTasksTest {

    @InjectMocks
    private PublishOvertimeRecurringTasks publishOvertimeRecurringTasks;

    @Mock
    private RecurringTaskService recurringTaskService;

    @Mock
    private ReminderEventMapper reminderEventMapper;

    @Mock
    private CancellationEventMapper cancellationEventMapper;

    @Mock
    private EventPublisher eventPublisher;

    @Test
    public void publishOvertimeRecurringTasks() {
        //data set
        Event overtimeEvent = new Event(PortalHousagotchi.APPLICATION_NAME, "abc", LocalDateTime.now(), new HashMap<>());
        Event seriouslyOvertimeEvent = new Event(PortalHousagotchi.APPLICATION_NAME, "def", LocalDateTime.now(), new HashMap<>());
        Event cancellationEvent = new Event(PortalHousagotchi.APPLICATION_NAME, "def", LocalDateTime.now(), new HashMap<>());

        RecurringTaskDTO recurringTask1 = new RecurringTaskDTO(1L, "1", 1, 2, LocalDate.now());
        RecurringTaskDTO recurringTask2 = new RecurringTaskDTO(2L, "2", 2, 3, LocalDate.now().minusDays(2));
        RecurringTaskDTO recurringTask3 = new RecurringTaskDTO(3L, "3", 3, 4, LocalDate.now().minusDays(1));
        RecurringTaskDTO recurringTask4 = new RecurringTaskDTO(4L, "4", 4, 5, LocalDate.now().minusDays(5));

        //mock
        doReturn(Arrays.asList(recurringTask1, recurringTask2, recurringTask3, recurringTask4)).when(recurringTaskService).findAll();
        doReturn(overtimeEvent).when(reminderEventMapper).map(recurringTask2);
        doReturn(seriouslyOvertimeEvent).when(reminderEventMapper).map(recurringTask4);
        doReturn(cancellationEvent).when(cancellationEventMapper).map(recurringTask4);

        //execute
        publishOvertimeRecurringTasks.publishOvertimeRecurringTasks();

        //verify
        verify(recurringTaskService, times(2)).findAll();

        verify(cancellationEventMapper).map(recurringTask4);
        verify(eventPublisher).publish(Stream.of(cancellationEvent).collect(Collectors.toSet()));

        verify(reminderEventMapper).map(recurringTask2);
        verify(reminderEventMapper).map(recurringTask4);
        verify(eventPublisher).publish(Stream.of(overtimeEvent, seriouslyOvertimeEvent).collect(Collectors.toSet()));

        verifyNoMoreInteractions(recurringTaskService, reminderEventMapper, eventPublisher, cancellationEventMapper);
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
        verifyNoMoreInteractions(recurringTaskService, reminderEventMapper, eventPublisher, cancellationEventMapper);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void publishOvertimeRecurringTasksWhenThereThereAreNoExecutions() {
        //data set
        Event seriouslyOvertimeEvent = new Event(PortalHousagotchi.APPLICATION_NAME, "abc", LocalDateTime.now(), new HashMap<>());
        Event cancellationEvent = new Event(PortalHousagotchi.APPLICATION_NAME, "abc", LocalDateTime.now(), new HashMap<>());
        RecurringTaskDTO recurringTask1 = new RecurringTaskDTO(1L, "1", 1, 2, null);
        RecurringTaskDTO recurringTask2 = new RecurringTaskDTO(2L, "2", 2, 3, null);
        RecurringTaskDTO recurringTask3 = new RecurringTaskDTO(3L, "3", 3, 4, LocalDate.now().minusDays(1));
        RecurringTaskDTO recurringTask4 = new RecurringTaskDTO(4L, "4", 4, 5, LocalDate.now().minusDays(5));

        //mock
        doReturn(Arrays.asList(recurringTask1, recurringTask2, recurringTask3, recurringTask4)).when(recurringTaskService).findAll();
        doReturn(seriouslyOvertimeEvent).when(reminderEventMapper).map(recurringTask4);
        doReturn(cancellationEvent).when(cancellationEventMapper).map(recurringTask4);

        //execute
        publishOvertimeRecurringTasks.publishOvertimeRecurringTasks();

        //verify
        ArgumentCaptor<Collection<Event>> eventPublisherArgumentCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(recurringTaskService, times(2)).findAll();

        verify(cancellationEventMapper).map(recurringTask4);
        verify(reminderEventMapper).map(recurringTask4);
        verify(eventPublisher, times(2)).publish(eventPublisherArgumentCaptor.capture());

        verifyNoMoreInteractions(recurringTaskService, reminderEventMapper, eventPublisher, cancellationEventMapper);

        assertEquals(2, eventPublisherArgumentCaptor.getAllValues().size());
        assertEquals(Stream.of(cancellationEvent).collect(Collectors.toSet()), eventPublisherArgumentCaptor.getAllValues().get(0));
        assertEquals(Stream.of(seriouslyOvertimeEvent).collect(Collectors.toSet()), eventPublisherArgumentCaptor.getAllValues().get(1));
    }
}
