package be.stijnhooft.portal.recurringtasks.mappers;

import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDto;
import be.stijnhooft.portal.recurringtasks.model.Execution;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ExecutionMapper extends Mapper<ExecutionDto, Execution> {

    @Override
    public Execution map(@NonNull ExecutionDto executionDTO) {
        return new Execution(
                executionDTO.getDate()
        );
    }

}
