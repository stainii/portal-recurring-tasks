package be.stijnhooft.portal.recurringtasks.services;

import be.stijnhooft.portal.model.domain.Event;
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
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional @Service
public class RecurringTaskService {

    private final RecurringTaskRepository recurringTaskRepository;
    private final RecurringTaskDtoMapper recurringTaskDTOMapper;
    private final RecurringTaskMapper recurringTaskMapper;
    private final ExecutionEventMapper executionEventMapper;
    private final ExecutionMapper executionMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public RecurringTaskService(RecurringTaskRepository recurringTaskRepository,
                                RecurringTaskDtoMapper recurringTaskDTOMapper,
                                ExecutionEventMapper executionEventMapper,
                                RecurringTaskMapper recurringTaskMapper,
                                ExecutionMapper executionMapper,
                                EventPublisher eventPublisher) {
        this.recurringTaskRepository = recurringTaskRepository;
        this.recurringTaskDTOMapper = recurringTaskDTOMapper;
        this.recurringTaskMapper = recurringTaskMapper;
        this.executionMapper = executionMapper;
        this.executionEventMapper = executionEventMapper;
        this.eventPublisher = eventPublisher;
    }

    public List<RecurringTaskDto> findAll() {
        List<RecurringTask> recurringTasks = recurringTaskRepository.findAll();
        return recurringTaskDTOMapper.mapAsList(recurringTasks);
    }

    public Optional<RecurringTaskDto> findById(long id) {
        return recurringTaskRepository.findById(id)
                .map(recurringTaskDTOMapper::map);
    }

    public RecurringTaskDto create(@NonNull RecurringTaskDto recurringTaskDTO) {
        RecurringTask newRecurringTask = recurringTaskMapper.map(recurringTaskDTO);
        RecurringTask savedNewRecurringTask = recurringTaskRepository.saveAndFlush(newRecurringTask);
        return recurringTaskDTOMapper.map(savedNewRecurringTask);
    }

    public RecurringTaskDto update(@NonNull RecurringTaskDto recurringTaskDTO) {
        RecurringTask recurringTask = recurringTaskRepository.findById(recurringTaskDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find recurring task with id " + recurringTaskDTO.getId()));

        recurringTask.update(recurringTaskDTO.getName(),
                recurringTaskDTO.getMinNumberOfDaysBetweenExecutions(),
                recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions());

        return recurringTaskDTOMapper.map(recurringTask);
    }

    public void delete(long id) {
        recurringTaskRepository.deleteById(id);
    }

    public RecurringTaskDto addExecution(@NonNull ExecutionDto executionDto, long recurringTaskId) {
        RecurringTask recurringTask = recurringTaskRepository.findById(recurringTaskId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find recurring task with id " + recurringTaskId));

        Execution execution = executionMapper.map(executionDto);
        recurringTask.addExecution(execution);

        RecurringTaskDto result = recurringTaskDTOMapper.map(recurringTask);
        if (executionDto.getSource() == Source.USER) {
            publishExecutionEvent(result);
        }
        return result;
    }

    private void publishExecutionEvent(RecurringTaskDto result) {
        Event executionEvent = executionEventMapper.map(result);
        eventPublisher.publish(Collections.singleton(executionEvent));
    }
}
