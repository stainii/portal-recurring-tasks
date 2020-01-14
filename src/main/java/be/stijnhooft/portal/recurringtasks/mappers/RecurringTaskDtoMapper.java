package be.stijnhooft.portal.recurringtasks.mappers;

import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import be.stijnhooft.portal.recurringtasks.model.Execution;
import be.stijnhooft.portal.recurringtasks.model.RecurringTask;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

@Component
public class RecurringTaskDtoMapper extends Mapper<RecurringTask, RecurringTaskDto> {

    @Override
    public RecurringTaskDto map(@NonNull RecurringTask recurringTask) {
        LocalDate lastExecutionDate = getLastExecutionDate(recurringTask);

        return new RecurringTaskDto(
                recurringTask.getId(),
                recurringTask.getName(),
                recurringTask.getMinNumberOfDaysBetweenExecutions(),
                recurringTask.getMaxNumberOfDaysBetweenExecutions(),
                lastExecutionDate
        );
    }

    private LocalDate getLastExecutionDate(@NonNull RecurringTask recurringTask) {
        Optional<Execution> lastExecution = recurringTask.getExecutions().stream()
                .max(Comparator.comparing(Execution::getDate));

        return lastExecution.map(Execution::getDate)
                .orElse(null);
    }
}
