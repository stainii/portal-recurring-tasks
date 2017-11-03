package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class RecurringTaskMapperTest {

    private RecurringTaskMapper mapper;

    @Before
    public void setUp() throws Exception {
        this.mapper = new RecurringTaskMapper();
    }

    @Test
    public void map() {
        RecurringTaskDTO recurringTaskDTO = new RecurringTaskDTO(1, "test", 1, 3, LocalDateTime.now());

        RecurringTask recurringTask = mapper.map(recurringTaskDTO);

        assertEquals(0, recurringTask.getId());
        assertEquals("test", recurringTask.getName());
        assertEquals(1, recurringTask.getMinNumberOfDaysBetweenExecutions());
        assertEquals(3, recurringTask.getMaxNumberOfDaysBetweenExecutions());
    }

    @Test(expected = NullPointerException.class)
    public void mapWhenPassingNull() {
        mapper.map((RecurringTaskDTO) null);
    }

}