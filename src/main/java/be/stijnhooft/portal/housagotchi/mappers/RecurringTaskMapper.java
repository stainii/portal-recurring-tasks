package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RecurringTaskMapper extends Mapper<RecurringTaskDTO, RecurringTask> {

    @Override
    public RecurringTask map(@NonNull RecurringTaskDTO recurringTaskDTO) {
        return new RecurringTask(
                recurringTaskDTO.getName(),
                recurringTaskDTO.getMinNumberOfDaysBetweenExecutions(),
                recurringTaskDTO.getMaxNumberOfDaysBetweenExecutions()
        );
    }

}
