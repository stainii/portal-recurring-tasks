package be.stijnhooft.portal.housagotchi.services;

import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.mappers.ExecutionMapper;
import be.stijnhooft.portal.housagotchi.mappers.RecurringTaskDTOMapper;
import be.stijnhooft.portal.housagotchi.mappers.RecurringTaskMapper;
import be.stijnhooft.portal.housagotchi.model.Execution;
import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import be.stijnhooft.portal.housagotchi.repositories.RecurringTaskRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Transactional @Service
public class RecurringTaskService {

    private final RecurringTaskRepository recurringTaskRepository;
    private final RecurringTaskDTOMapper recurringTaskDTOMapper;
    private final RecurringTaskMapper recurringTaskMapper;
    private final ExecutionMapper executionMapper;

    @Inject
    public RecurringTaskService(RecurringTaskRepository recurringTaskRepository, RecurringTaskDTOMapper recurringTaskDTOMapper, RecurringTaskMapper recurringTaskMapper, ExecutionMapper executionMapper) {
        this.recurringTaskRepository = recurringTaskRepository;
        this.recurringTaskDTOMapper = recurringTaskDTOMapper;
        this.recurringTaskMapper = recurringTaskMapper;
        this.executionMapper = executionMapper;
    }

    public List<RecurringTaskDTO> findAll() {
        List<RecurringTask> recurringTasks = recurringTaskRepository.findAll();
        return recurringTaskDTOMapper.mapAsList(recurringTasks);
    }

    public Optional<RecurringTaskDTO> findById(long id) {
        RecurringTask recurringTask = recurringTaskRepository.findOne(id);
        if (recurringTask == null) {
            return Optional.empty();
        } else {
            return Optional.of(recurringTaskDTOMapper.map(recurringTask));
        }
    }

    public RecurringTaskDTO create(@NonNull RecurringTaskDTO recurringTaskDTO) {
        RecurringTask newRecurringTask = recurringTaskMapper.map(recurringTaskDTO);
        RecurringTask savedNewReccuringTask = recurringTaskRepository.saveAndFlush(newRecurringTask);
        return recurringTaskDTOMapper.map(savedNewReccuringTask);
    }

    public RecurringTaskDTO update(@NonNull RecurringTaskDTO recurringTaskDTO) {
        RecurringTask recurringTaskToUpdate = recurringTaskRepository.findOne(recurringTaskDTO.getId());

        if (recurringTaskToUpdate == null ) {
            throw new IllegalArgumentException("Cannot find recurring task with id " + recurringTaskDTO.getId());
        }

        recurringTaskToUpdate.update(recurringTaskDTO.getName(),
                recurringTaskDTO.getMinNumberOfDaysBetweenExecutions(),
                recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions());

        return recurringTaskDTOMapper.map(recurringTaskToUpdate);
    }

    public void delete(long id) {
        recurringTaskRepository.delete(id);
    }

    public RecurringTaskDTO addExecution(@NonNull ExecutionDTO executionDTO, long recurringTaskId) {
        RecurringTask recurringTask = recurringTaskRepository.findOne(recurringTaskId);
        if (recurringTask == null ) {
            throw new IllegalArgumentException("Cannot find recurring task with id " + recurringTaskId);
        }

        Execution execution = executionMapper.map(executionDTO);
        recurringTask.addExecution(execution);

        return recurringTaskDTOMapper.map(recurringTask);
    }
}
