package be.stijnhooft.portal.recurringtasks.mappers;

import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import be.stijnhooft.portal.recurringtasks.model.RecurringTask;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RecurringTaskMapper extends Mapper<RecurringTaskDto, RecurringTask> {

    @Override
    public RecurringTask map(@NonNull RecurringTaskDto recurringTaskDTO) {
        return new RecurringTask(
                recurringTaskDTO.getName(),
                recurringTaskDTO.getMinNumberOfDaysBetweenExecutions(),
                recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions()
        );
    }

}
