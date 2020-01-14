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

public class CancellationEventMapperTest {

    private CancellationEventMapper eventMapper;
    private final static String DEPLOYMENT_NAME = "TestApplication";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        eventMapper = new CancellationEventMapper(DEPLOYMENT_NAME);
    }

    @Test
    public void mapCancellationWhenNull() {
        expectedException.expect(NullPointerException.class);
        eventMapper.map((RecurringTaskDto) null);
    }

    @Test
    public void mapCancellation() {
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);
        RecurringTaskDto recurringTaskDTO = new RecurringTaskDto(
                1L, "Do the dishes", 2,
                3, twoDaysAgo);

        Event event = eventMapper.map(recurringTaskDTO);

        assertEquals(DEPLOYMENT_NAME, event.getSource());
        assertNotNull(event.getPublishDate());
        assertEquals(DEPLOYMENT_NAME + "-1", event.getFlowId());

        Map<String, String> data = event.getData();
        assertEquals("cancellation", data.get("type"));
    }

}