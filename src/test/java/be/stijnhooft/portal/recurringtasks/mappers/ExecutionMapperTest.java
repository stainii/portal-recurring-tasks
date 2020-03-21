package be.stijnhooft.portal.recurringtasks.mappers;

import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDto;
import be.stijnhooft.portal.recurringtasks.dtos.Source;
import be.stijnhooft.portal.recurringtasks.model.Execution;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ExecutionMapperTest {

    private ExecutionMapper mapper;

    @Before
    public void init() {
        mapper = new ExecutionMapper();
    }

    @Test(expected = NullPointerException.class)
    public void mapWhenPassingNull() {
        mapper.map((ExecutionDto) null);
    }

    @Test
    public void map() {
        LocalDate date = LocalDate.now();
        ExecutionDto executionDTO = new ExecutionDto(date, Source.USER);

        Execution execution = mapper.map(executionDTO);

        assertEquals(date, execution.getDate());
    }
}