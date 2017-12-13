package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.model.Execution;
import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecurringTaskDTOMapperTest {

    private RecurringTaskDTOMapper mapper;

    @Before
    public void setUp() throws Exception {
        this.mapper = new RecurringTaskDTOMapper();
    }

    @Test
    public void mapWhenNoExecutionsExist() throws Exception {
        RecurringTask recurringTask = new RecurringTask("test", 1, 3);
        RecurringTaskDTO recurringTaskDTO = mapper.map(recurringTask);

        assertEquals("test", recurringTaskDTO.getName());
        assertEquals(1, recurringTaskDTO.getMinNumberOfDaysBetweenExecutions());
        assertEquals(3, recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions());
        assertNull(recurringTaskDTO.getLastExecution());
    }

    @Test
    public void mapWhenExecutionsExist() throws Exception {
        RecurringTask recurringTask = new RecurringTask("test", 1, 3) {
            RecurringTask setId(long id) {
                this.id = id;
                return this;
            }
        }.setId(10);

        recurringTask.addExecution(new Execution(LocalDateTime.of(2017, Month.OCTOBER, 23, 10, 12)));
        recurringTask.addExecution(new Execution(LocalDateTime.of(2017, Month.OCTOBER, 23, 15, 3)));
        recurringTask.addExecution(new Execution(LocalDateTime.of(2017, Month.OCTOBER, 10, 15, 3)));

        RecurringTaskDTO recurringTaskDTO = mapper.map(recurringTask);

        assertEquals(Long.valueOf(10), recurringTaskDTO.getId());
        assertEquals("test", recurringTaskDTO.getName());
        assertEquals(1, recurringTaskDTO.getMinNumberOfDaysBetweenExecutions());
        assertEquals(3, recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions());
        assertEquals(LocalDateTime.of(2017, Month.OCTOBER, 23, 15, 3), recurringTaskDTO.getLastExecution());
    }

    @Test(expected = NullPointerException.class)
    public void mapWhenPassingNull() {
        mapper.map((RecurringTask) null);
    }

}