package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.model.Execution;
import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Component
public class RecurringTaskDTOMapper extends Mapper<RecurringTask, RecurringTaskDTO> {

    @Override
    public RecurringTaskDTO map(@NonNull RecurringTask recurringTask) {
        LocalDateTime lastExecutionDate = getLastExecutionDate(recurringTask);

        return new RecurringTaskDTO(
                recurringTask.getId(),
                recurringTask.getName(),
                recurringTask.getMinNumberOfDaysBetweenExecutions(),
                recurringTask.getMaxNumberOfDaysBetweenExecutions(),
                lastExecutionDate
        );
    }

    private LocalDateTime getLastExecutionDate(@NonNull RecurringTask recurringTask) {
        Optional<Execution> lastExecution = recurringTask.getExecutions().stream()
                .max(Comparator.comparing(Execution::getDate));

        return lastExecution.isPresent() ?
                lastExecution.get().getDate() : null;
    }
}
