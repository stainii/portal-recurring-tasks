package be.stijnhooft.portal.recurringtasks.mappers;

import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDTO;
import be.stijnhooft.portal.recurringtasks.model.Execution;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ExecutionMapper extends Mapper<ExecutionDTO, Execution> {

    @Override
    public Execution map(@NonNull ExecutionDTO executionDTO) {
        return new Execution(
                executionDTO.getDate()
        );
    }

}
