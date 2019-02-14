package be.stijnhooft.portal.housagotchi.services;

import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.ExecutionMapper;
import be.stijnhooft.portal.housagotchi.mappers.RecurringTaskDTOMapper;
import be.stijnhooft.portal.housagotchi.mappers.RecurringTaskMapper;
import be.stijnhooft.portal.housagotchi.mappers.event.ExecutionEventMapper;
import be.stijnhooft.portal.housagotchi.messaging.EventPublisher;
import be.stijnhooft.portal.housagotchi.model.Execution;
import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import be.stijnhooft.portal.housagotchi.repositories.RecurringTaskRepository;
import be.stijnhooft.portal.model.domain.Event;
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
    private final RecurringTaskDTOMapper recurringTaskDTOMapper;
    private final RecurringTaskMapper recurringTaskMapper;
    private final ExecutionEventMapper executionEventMapper;
    private final ExecutionMapper executionMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public RecurringTaskService(RecurringTaskRepository recurringTaskRepository,
                                RecurringTaskDTOMapper recurringTaskDTOMapper,
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

    public List<RecurringTaskDTO> findAll() {
        List<RecurringTask> recurringTasks = recurringTaskRepository.findAll();
        return recurringTaskDTOMapper.mapAsList(recurringTasks);
    }

    public Optional<RecurringTaskDTO> findById(long id) {
        return recurringTaskRepository.findById(id)
                .map(recurringTaskDTOMapper::map);
    }

    public RecurringTaskDTO create(@NonNull RecurringTaskDTO recurringTaskDTO) {
        RecurringTask newRecurringTask = recurringTaskMapper.map(recurringTaskDTO);
        RecurringTask savedNewReccuringTask = recurringTaskRepository.saveAndFlush(newRecurringTask);
        return recurringTaskDTOMapper.map(savedNewReccuringTask);
    }

    public RecurringTaskDTO update(@NonNull RecurringTaskDTO recurringTaskDTO) {
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

    public RecurringTaskDTO addExecution(@NonNull ExecutionDTO executionDTO, long recurringTaskId) {
        Optional<RecurringTask> recurringTaskOptional = recurringTaskRepository.findById(recurringTaskId);
        if (!recurringTaskOptional.isPresent()) {
            throw new IllegalArgumentException("Cannot find recurring task with id " + recurringTaskId);
        } else {
            RecurringTask recurringTask = recurringTaskOptional.get();

            Execution execution = executionMapper.map(executionDTO);
            recurringTask.addExecution(execution);

            RecurringTaskDTO result = recurringTaskDTOMapper.map(recurringTask);
            publishExecutionEvent(result);
            return result;
        }

    }

    private void publishExecutionEvent(RecurringTaskDTO result) {
        Event executionEvent = executionEventMapper.map(result);
        eventPublisher.publish(Collections.singleton(executionEvent));
    }
}
