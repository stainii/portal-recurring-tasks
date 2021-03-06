package be.stijnhooft.portal.recurringtasks.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDto;
import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import be.stijnhooft.portal.recurringtasks.dtos.Source;
import be.stijnhooft.portal.recurringtasks.mappers.ExecutionMapper;
import be.stijnhooft.portal.recurringtasks.mappers.RecurringTaskDtoMapper;
import be.stijnhooft.portal.recurringtasks.mappers.RecurringTaskMapper;
import be.stijnhooft.portal.recurringtasks.mappers.event.ExecutionEventMapper;
import be.stijnhooft.portal.recurringtasks.messaging.EventPublisher;
import be.stijnhooft.portal.recurringtasks.model.Execution;
import be.stijnhooft.portal.recurringtasks.model.RecurringTask;
import be.stijnhooft.portal.recurringtasks.repositories.RecurringTaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecurringTaskServiceTest {

    @InjectMocks
    private RecurringTaskService recurringTaskService;

    @Mock
    private RecurringTaskRepository recurringTaskRepository;

    @Mock
    private RecurringTaskDtoMapper recurringTaskDtoMapper;

    @Mock
    private RecurringTaskMapper recurringTaskMapper;

    @Mock
    private ExecutionMapper executionMapper;

    @Mock
    private ExecutionEventMapper executionEventMapper;

    @Mock
    private EventPublisher eventPublisher;

    @Test
    public void findAll() {
        //data set
        List<RecurringTask> recurringTasks = Arrays.asList(new RecurringTask());
        List<RecurringTaskDto> recurringTaskDtos = Arrays.asList(new RecurringTaskDto(1L, "test", 1, 2, null));

        //mocking
        doReturn(recurringTasks).when(recurringTaskRepository).findAll();
        doReturn(recurringTaskDtos).when(recurringTaskDtoMapper).mapAsList(recurringTasks);

        //execute
        List<RecurringTaskDto> result = recurringTaskService.findAll();

        //verify and assert
        verify(recurringTaskRepository).findAll();
        verify(recurringTaskDtoMapper).mapAsList(recurringTasks);
        verifyNoMoreInteractions(recurringTaskDtoMapper, recurringTaskMapper, recurringTaskRepository, executionEventMapper, executionMapper, eventPublisher);

        assertSame(recurringTaskDtos, result);
    }

    @Test(expected = NullPointerException.class)
    public void createWhenPassingNull() {
        recurringTaskService.create(null);
    }

    @Test
    public void create() {
        //data set
        RecurringTaskDto recurringTaskDTO = new RecurringTaskDto(0L, "test", 1, 3, null);
        RecurringTask recurringTask = new RecurringTask();
        RecurringTask savedRecurringTask = new RecurringTask();
        RecurringTaskDto savedRecurringTaskDto = new RecurringTaskDto(1L, "test", 1, 3, null);

        //mock
        doReturn(recurringTask).when(recurringTaskMapper).map(recurringTaskDTO);
        doReturn(savedRecurringTask).when(recurringTaskRepository).saveAndFlush(recurringTask);
        doReturn(savedRecurringTaskDto).when(recurringTaskDtoMapper).map(savedRecurringTask);

        //execute
        RecurringTaskDto result = recurringTaskService.create(recurringTaskDTO);

        //verify and assert
        verify(recurringTaskMapper).map(recurringTaskDTO);
        verify(recurringTaskRepository).saveAndFlush(recurringTask);
        verify(recurringTaskDtoMapper).map(savedRecurringTask);
        verifyNoMoreInteractions(recurringTaskDtoMapper, recurringTaskMapper, recurringTaskRepository, executionEventMapper, executionMapper, eventPublisher);

        assertSame(savedRecurringTaskDto, result);
    }

    @Test(expected = NullPointerException.class)
    public void updateWhenPassingNull() {
        recurringTaskService.update(null);
    }

    @Test
    public void update() {
        //data set
        RecurringTaskDto recurringTaskDTO = new RecurringTaskDto(10L, "newName", 2, 3, null);
        RecurringTaskDto newRecurringTaskDto = new RecurringTaskDto(10L, "newName", 2, 3, null);
        RecurringTask recurringTask = new RecurringTask("oldName", 1, 1);

        //mock
        doReturn(Optional.of(recurringTask)).when(recurringTaskRepository).findById(10L);
        doReturn(newRecurringTaskDto).when(recurringTaskDtoMapper).map(recurringTask);

        //execute
        RecurringTaskDto result = recurringTaskService.update(recurringTaskDTO);

        //verify and assert
        verify(recurringTaskRepository).findById(10L);
        verify(recurringTaskDtoMapper).map(recurringTask);
        verifyNoMoreInteractions(recurringTaskDtoMapper, recurringTaskMapper, recurringTaskRepository, executionEventMapper, executionMapper, eventPublisher);

        assertSame(newRecurringTaskDto, result);
    }

    @Test
    public void deleteWhenSuccess() {
        recurringTaskService.delete(10L);
        verify(recurringTaskRepository).deleteById(10L);
    }

    @Test(expected = NullPointerException.class)
    public void addExecutionWhenPassingNull() {
        recurringTaskService.addExecution(null, 1);
    }

    @Test
    public void addExecutionWhenSourceIsUser() {
        //data set
        ExecutionDto executionDTO = new ExecutionDto(LocalDate.now(), Source.USER);
        Execution execution = new Execution();
        RecurringTask recurringTask = new RecurringTask();
        RecurringTaskDto recurringTaskDTO = new RecurringTaskDto(10L, "test", 1, 2, null);
        Event executionEvent = new Event("Housagotchi", "Housagotchi-1", FlowAction.END, LocalDateTime.now(), new HashMap<>());

        //mock
        doReturn(execution).when(executionMapper).map(executionDTO);
        doReturn(Optional.of(recurringTask)).when(recurringTaskRepository).findById(10L);
        doReturn(recurringTaskDTO).when(recurringTaskDtoMapper).map(recurringTask);
        doReturn(executionEvent).when(executionEventMapper).map(recurringTaskDTO);

        //execute
        RecurringTaskDto result = recurringTaskService.addExecution(executionDTO, 10L);

        //verify and assert
        verify(executionMapper).map(executionDTO);
        verify(recurringTaskRepository).findById(10L);
        verify(recurringTaskDtoMapper).map(recurringTask);
        verify(executionEventMapper).map(recurringTaskDTO);
        verify(eventPublisher).publish(Collections.singleton(executionEvent));
        verifyNoMoreInteractions(recurringTaskDtoMapper, recurringTaskMapper, recurringTaskRepository, executionEventMapper, executionMapper, eventPublisher);

        assertSame(recurringTaskDTO, result);
        assertEquals(1, recurringTask.getExecutions().size());
        assertSame(execution, recurringTask.getExecutions().get(0));
    }

    @Test
    public void addExecutionWhenSourceIsEvent() {
        //data set
        ExecutionDto executionDto = new ExecutionDto(LocalDate.now(), Source.EVENT);
        Execution execution = new Execution();
        RecurringTask recurringTask = new RecurringTask();
        RecurringTaskDto recurringTaskDto = new RecurringTaskDto(10L, "test", 1, 2, null);
        Event executionEvent = new Event("Housagotchi", "Housagotchi-1", FlowAction.END, LocalDateTime.now(), new HashMap<>());

        //mock
        doReturn(execution).when(executionMapper).map(executionDto);
        doReturn(Optional.of(recurringTask)).when(recurringTaskRepository).findById(10L);
        doReturn(recurringTaskDto).when(recurringTaskDtoMapper).map(recurringTask);

        //execute
        RecurringTaskDto result = recurringTaskService.addExecution(executionDto, 10L);

        //verify and assert
        verify(executionMapper).map(executionDto);
        verify(recurringTaskRepository).findById(10L);
        verify(recurringTaskDtoMapper).map(recurringTask);
        verifyNoMoreInteractions(recurringTaskDtoMapper, recurringTaskMapper, recurringTaskRepository, executionEventMapper, executionMapper, eventPublisher);

        assertSame(recurringTaskDto, result);
        assertEquals(1, recurringTask.getExecutions().size());
        assertSame(execution, recurringTask.getExecutions().get(0));
    }

    @Test
    public void findById() {
        //data set
        RecurringTask recurringTask = new RecurringTask("test", 1, 3);
        RecurringTaskDto recurringTaskDTO = new RecurringTaskDto(10L, "test", 2, 3, null);

        //mock
        doReturn(Optional.of(recurringTask)).when(recurringTaskRepository).findById(10L);
        doReturn(recurringTaskDTO).when(recurringTaskDtoMapper).map(recurringTask);

        //execute
        Optional<RecurringTaskDto> result = recurringTaskService.findById(10L);

        //verify and assert
        verify(recurringTaskRepository).findById(10L);
        verify(recurringTaskDtoMapper).map(recurringTask);
        verifyNoMoreInteractions(recurringTaskDtoMapper, recurringTaskMapper, recurringTaskRepository, executionEventMapper, executionMapper, eventPublisher);

        assertSame(recurringTaskDTO, result.get());
    }

    @Test
    public void findByIdWhenNotFound() {
        //mock
        doReturn(Optional.empty()).when(recurringTaskRepository).findById(10L);

        //execute
        Optional<RecurringTaskDto> result = recurringTaskService.findById(10L);

        //verify and assert
        verify(recurringTaskRepository).findById(10L);
        verifyNoMoreInteractions(recurringTaskDtoMapper, recurringTaskMapper, recurringTaskRepository, executionEventMapper, executionMapper, eventPublisher);

        assertFalse(result.isPresent());
    }
}
