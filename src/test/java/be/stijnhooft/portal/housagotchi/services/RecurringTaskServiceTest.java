package be.stijnhooft.portal.housagotchi.services;

import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.ExecutionMapper;
import be.stijnhooft.portal.housagotchi.mappers.RecurringTaskDTOMapper;
import be.stijnhooft.portal.housagotchi.mappers.RecurringTaskMapper;
import be.stijnhooft.portal.housagotchi.model.Execution;
import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import be.stijnhooft.portal.housagotchi.repositories.RecurringTaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecurringTaskServiceTest {

    @InjectMocks
    private RecurringTaskService recurringTaskService;

    @Mock
    private RecurringTaskRepository recurringTaskRepository;

    @Mock
    private RecurringTaskDTOMapper recurringTaskDTOMapper;

    @Mock
    private RecurringTaskMapper recurringTaskMapper;

    @Mock
    private ExecutionMapper executionMapper;

    @Test
    public void findAll() throws Exception {
        //data set
        List<RecurringTask> recurringTasks = Arrays.asList(new RecurringTask());
        List<RecurringTaskDTO> recurringTaskDTOs = Arrays.asList(new RecurringTaskDTO(Long.valueOf(1), "test", 1, 2, null));

        //mocking
        doReturn(recurringTasks).when(recurringTaskRepository).findAll();
        doReturn(recurringTaskDTOs).when(recurringTaskDTOMapper).mapAsList(recurringTasks);

        //execute
        List<RecurringTaskDTO> result = recurringTaskService.findAll();

        //verify and assert
        verify(recurringTaskRepository).findAll();
        verify(recurringTaskDTOMapper).mapAsList(recurringTasks);

        assertSame(recurringTaskDTOs, result);
    }

    @Test(expected = NullPointerException.class)
    public void createWhenPassingNull() {
        recurringTaskService.create(null);
    }

    @Test
    public void create() {
        //data set
        RecurringTaskDTO recurringTaskDTO = new RecurringTaskDTO(Long.valueOf(0), "test", 1, 3, null);
        RecurringTask recurringTask = new RecurringTask();
        RecurringTask savedRecurringTask = new RecurringTask();
        RecurringTaskDTO savedRecurringTaskDTO = new RecurringTaskDTO(Long.valueOf(1), "test", 1, 3, null);

        //mock
        doReturn(recurringTask).when(recurringTaskMapper).map(recurringTaskDTO);
        doReturn(savedRecurringTask).when(recurringTaskRepository).saveAndFlush(recurringTask);
        doReturn(savedRecurringTaskDTO).when(recurringTaskDTOMapper).map(savedRecurringTask);

        //execute
        RecurringTaskDTO result = recurringTaskService.create(recurringTaskDTO);

        //verify and assert
        verify(recurringTaskMapper).map(recurringTaskDTO);
        verify(recurringTaskRepository).saveAndFlush(recurringTask);
        verify(recurringTaskDTOMapper).map(savedRecurringTask);

        assertSame(savedRecurringTaskDTO, result);
    }

    @Test(expected = NullPointerException.class)
    public void updateWhenPassingNull() {
        recurringTaskService.update(null);
    }

    @Test
    public void update() {
        //data set
        RecurringTaskDTO recurringTaskDTO = new RecurringTaskDTO(10L, "newName", 2, 3, null);
        RecurringTaskDTO newRecurringTaskDTO = new RecurringTaskDTO(10L, "newName", 2, 3, null);
        RecurringTask recurringTask = new RecurringTask("oldName", 1, 1);

        //mock
        doReturn(recurringTask).when(recurringTaskRepository).findOne(10L);
        doReturn(newRecurringTaskDTO).when(recurringTaskDTOMapper).map(recurringTask);

        //execute
        RecurringTaskDTO result = recurringTaskService.update(recurringTaskDTO);

        //verify and assert
        verify(recurringTaskRepository).findOne(10L);
        verify(recurringTaskDTOMapper).map(recurringTask);

        assertSame(newRecurringTaskDTO, result);
    }

    @Test
    public void deleteWhenSuccess() {
        recurringTaskService.delete(10L);
        verify(recurringTaskRepository).delete(10L);
    }

    @Test(expected = NullPointerException.class)
    public void addExecutionWhenPassingNull() {
        recurringTaskService.addExecution(null, 1);
    }

    @Test
    public void addExecution() {
        //data set
        ExecutionDTO executionDTO = new ExecutionDTO(LocalDateTime.now());
        Execution execution = new Execution();
        RecurringTask recurringTask = new RecurringTask();
        RecurringTaskDTO recurringTaskDTO = new RecurringTaskDTO(10L, "test", 1, 2, null);

        //mock
        doReturn(execution).when(executionMapper).map(executionDTO);
        doReturn(recurringTask).when(recurringTaskRepository).findOne(10L);
        doReturn(recurringTaskDTO).when(recurringTaskDTOMapper).map(recurringTask);

        //execute
        RecurringTaskDTO result = recurringTaskService.addExecution(executionDTO, 10L);

        //verify and assert
        verify(executionMapper).map(executionDTO);
        verify(recurringTaskRepository).findOne(10L);
        verify(recurringTaskDTOMapper).map(recurringTask);

        assertSame(recurringTaskDTO, result);
        assertEquals(1, recurringTask.getExecutions().size());
        assertSame(execution, recurringTask.getExecutions().get(0));
    }

    @Test
    public void findById() {
        //data set
        RecurringTask recurringTask = new RecurringTask("test", 1, 3);
        RecurringTaskDTO recurringTaskDTO = new RecurringTaskDTO(10L, "test", 2, 3, null);

        //mock
        doReturn(recurringTask).when(recurringTaskRepository).findOne(10L);
        doReturn(recurringTaskDTO).when(recurringTaskDTOMapper).map(recurringTask);

        //execute
        Optional<RecurringTaskDTO> result = recurringTaskService.findById(10L);

        //verify and assert
        verify(recurringTaskRepository).findOne(10L);
        verify(recurringTaskDTOMapper).map(recurringTask);

        assertSame(recurringTaskDTO, result.get());
    }

    @Test
    public void findByIdWheNotFound() {
        //mock
        doReturn(null).when(recurringTaskRepository).findOne(10L);

        //execute
        Optional<RecurringTaskDTO> result = recurringTaskService.findById(10L);

        //verify and assert
        verify(recurringTaskRepository).findOne(10L);

        assertFalse(result.isPresent());
    }
}