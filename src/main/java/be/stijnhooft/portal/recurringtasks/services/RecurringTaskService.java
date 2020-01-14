package be.stijnhooft.portal.recurringtasks.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDTO;
import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
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
        RecurringTask savedNewReccuringTask = recurringTaskRepository.saveAndFlush(newRecurringTask);
        return recurringTaskDTOMapper.map(savedNewReccuringTask);
    }

    public RecurringTaskDto update(@NonNull RecurringTaskDto recurringTaskDTO) {
        Optional<RecurringTask> recurringTaskToUpdateOptional = recurringTaskRepository.findById(recurringTaskDTO.getId());

        if (!recurringTaskToUpdateOptional.isPresent()) {
            throw new IllegalArgumentException("Cannot find recurring task with id " + recurringTaskDTO.getId());
        }

        RecurringTask recurringTask = recurringTaskToUpdateOptional.get();
        recurringTask.update(recurringTaskDTO.getName(),
                recurringTaskDTO.getMinNumberOfDaysBetweenExecutions(),
                recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions());

        return recurringTaskDTOMapper.map(recurringTask);
    }

    public void delete(long id) {
        recurringTaskRepository.deleteById(id);
    }

    public RecurringTaskDto addExecution(@NonNull ExecutionDTO executionDTO, long recurringTaskId) {
        Optional<RecurringTask> recurringTaskOptional = recurringTaskRepository.findById(recurringTaskId);
        if (!recurringTaskOptional.isPresent()) {
            throw new IllegalArgumentException("Cannot find recurring task with id " + recurringTaskId);
        } else {
            RecurringTask recurringTask = recurringTaskOptional.get();

            Execution execution = executionMapper.map(executionDTO);
            recurringTask.addExecution(execution);

            RecurringTaskDto result = recurringTaskDTOMapper.map(recurringTask);
            publishExecutionEvent(result);
            return result;
        }

    }

    private void publishExecutionEvent(RecurringTaskDto result) {
        Event executionEvent = executionEventMapper.map(result);
        eventPublisher.publish(Collections.singleton(executionEvent));
    }
}
