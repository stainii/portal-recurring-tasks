package be.stijnhooft.portal.recurringtasks.messaging;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDto;
import be.stijnhooft.portal.recurringtasks.dtos.Source;
import be.stijnhooft.portal.recurringtasks.services.RecurringTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventTopicListenerTest {

    private EventTopicListener eventTopicListener;

    @Mock
    private RecurringTaskService recurringTaskService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        eventTopicListener = new EventTopicListener(recurringTaskService, "Housagotchi");
    }

    @Test
    public void receive() {
        var event1 = Event.builder()            // should be taken into account
                .flowAction(FlowAction.END)
                .flowId("Housagotchi-100")
                .publishDate(LocalDateTime.now())
                .source("Todo")
                .data(new HashMap<>())
                .build();
        var event2 = Event.builder()            // should not be taken into account, since the flow is not of this application
                .flowAction(FlowAction.END)
                .flowId("Health-101")
                .source("Todo")
                .publishDate(LocalDateTime.now())
                .data(new HashMap<>())
                .build();
        var event3 = Event.builder()            // should not be taken into account, since it is a start of a flow
                .flowAction(FlowAction.START)
                .flowId("Housagotchi-102")
                .publishDate(LocalDateTime.now())
                .source("Todo")
                .data(new HashMap<>())
                .build();
        var event4 = Event.builder()            // should not be taken into account, since the application has sent this event itself
                .flowAction(FlowAction.END)
                .flowId("Housagotchi-103")
                .publishDate(LocalDateTime.now())
                .source("Housagotchi")
                .data(new HashMap<>())
                .build();

        eventTopicListener.receive(List.of(event1, event2, event3, event4));

        ArgumentCaptor<ExecutionDto> executionCaptor = ArgumentCaptor.forClass(ExecutionDto.class);
        verify(recurringTaskService).addExecution(executionCaptor.capture(), eq(100L));

        assertThat(executionCaptor.getValue().getDate(), is(event1.getPublishDate().toLocalDate()));
        assertThat(executionCaptor.getValue().getSource(), is(Source.EVENT));
    }
}