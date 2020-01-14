package be.stijnhooft.portal.recurringtasks.mappers;

import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import be.stijnhooft.portal.recurringtasks.model.Execution;
import be.stijnhooft.portal.recurringtasks.model.RecurringTask;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecurringTaskDtoMapperTest {

    private RecurringTaskDtoMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new RecurringTaskDtoMapper();
    }

    @Test
    public void mapWhenNoExecutionsExist() {
        RecurringTask recurringTask = new RecurringTask("test", 1, 3);
        RecurringTaskDto recurringTaskDTO = mapper.map(recurringTask);

        assertEquals("test", recurringTaskDTO.getName());
        assertEquals(1, recurringTaskDTO.getMinNumberOfDaysBetweenExecutions());
        assertEquals(3, recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions());
        assertNull(recurringTaskDTO.getLastExecution());
    }

    @Test
    public void mapWhenExecutionsExist() {
        RecurringTask recurringTask = new RecurringTask("test", 1, 3) {
            RecurringTask setId(long id) {
                this.id = id;
                return this;
            }
        }.setId(10);

        recurringTask.addExecution(new Execution(LocalDate.of(2017, Month.OCTOBER, 23)));
        recurringTask.addExecution(new Execution(LocalDate.of(2017, Month.OCTOBER, 23)));
        recurringTask.addExecution(new Execution(LocalDate.of(2017, Month.OCTOBER, 10)));

        RecurringTaskDto recurringTaskDTO = mapper.map(recurringTask);

        assertEquals(Long.valueOf(10), recurringTaskDTO.getId());
        assertEquals("test", recurringTaskDTO.getName());
        assertEquals(1, recurringTaskDTO.getMinNumberOfDaysBetweenExecutions());
        assertEquals(3, recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions());
        assertEquals(LocalDate.of(2017, Month.OCTOBER, 23), recurringTaskDTO.getLastExecution());
    }

    @Test(expected = NullPointerException.class)
    public void mapWhenPassingNull() {
        mapper.map((RecurringTask) null);
    }

}