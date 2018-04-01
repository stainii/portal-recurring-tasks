package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.model.Execution;
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
        mapper.map((ExecutionDTO) null);
    }

    @Test
    public void map() {
        LocalDate date = LocalDate.now();
        ExecutionDTO executionDTO = new ExecutionDTO(date);

        Execution execution = mapper.map(executionDTO);

        assertEquals(date, execution.getDate());
    }
}