package be.stijnhooft.portal.recurringtasks.mappers.event;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReminderEventMapperTest {

    private ReminderEventMapper eventMapper;
    private final static String DEPLOYMENT_NAME = "TestApplication";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        eventMapper = new ReminderEventMapper(DEPLOYMENT_NAME);
    }

    @Test
    public void mapReminderWhenNull() {
        expectedException.expect(NullPointerException.class);
        eventMapper.map((RecurringTaskDto) null);
    }

    @Test
    public void mapReminderWhenNotUrgentTask() {
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);
        RecurringTaskDto recurringTaskDTO = new RecurringTaskDto(
                1L, "Do the dishes", 2,
                3, twoDaysAgo);

        Event event = eventMapper.map(recurringTaskDTO);

        assertEquals(DEPLOYMENT_NAME, event.getSource());
        assertNotNull(event.getPublishDate());
        assertEquals(DEPLOYMENT_NAME + "-1", event.getFlowId());

        Map<String, String> data = event.getData();
        assertEquals(Boolean.FALSE.toString(), data.get("urgent"));
        assertEquals("Do the dishes", data.get("task"));
        assertEquals(twoDaysAgo.toString(), data.get("lastExecution"));
        assertEquals("reminder", data.get("type"));
    }

    @Test
    public void mapReminderWhenUrgentTask() {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        RecurringTaskDto recurringTaskDTO = new RecurringTaskDto(
                1L, "Do the dishes", 2,
                3, threeDaysAgo);

        Event event = eventMapper.map(recurringTaskDTO);

        assertEquals(DEPLOYMENT_NAME, event.getSource());
        assertNotNull(event.getPublishDate());
        assertEquals(DEPLOYMENT_NAME + "-1", event.getFlowId());

        Map<String, String> data = event.getData();
        assertEquals(Boolean.TRUE.toString(), data.get("urgent"));
        assertEquals("Do the dishes", data.get("task"));
        assertEquals(threeDaysAgo.toString(), data.get("lastExecution"));
        assertEquals("reminder", data.get("type"));
    }
}