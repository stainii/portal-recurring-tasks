package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.PortalHousagotchiApplication;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.model.domain.Event;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventMapperTest {

    private EventMapper eventMapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        eventMapper = new EventMapper();
    }

    @Test
    public void mapNull() {
        expectedException.expect(NullPointerException.class);
        eventMapper.map((RecurringTaskDTO) null);
    }

    @Test
    public void mapNotUrgentTask() {
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);
        RecurringTaskDTO recurringTaskDTO = new RecurringTaskDTO(
                1L, "Do the dishes", 2,
                3, twoDaysAgo);

        Event event = eventMapper.map(recurringTaskDTO);

        assertEquals(PortalHousagotchiApplication.APPLICATION_NAME, event.getSource());
        assertNotNull(event.getPublishDate());

        Map<String, String> data = event.getData();
        assertEquals(Boolean.FALSE.toString(), data.get("urgent"));
        assertEquals("Do the dishes", data.get("task"));
        assertEquals(twoDaysAgo.toString(), data.get("lastExecution"));
    }

    @Test
    public void mapUrgentTask() {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        RecurringTaskDTO recurringTaskDTO = new RecurringTaskDTO(
                1L, "Do the dishes", 2,
                3, threeDaysAgo);

        Event event = eventMapper.map(recurringTaskDTO);

        assertEquals(PortalHousagotchiApplication.APPLICATION_NAME, event.getSource());
        assertNotNull(event.getPublishDate());

        Map<String, String> data = event.getData();
        assertEquals(Boolean.TRUE.toString(), data.get("urgent"));
        assertEquals("Do the dishes", data.get("task"));
        assertEquals(threeDaysAgo.toString(), data.get("lastExecution"));
    }
}