package be.stijnhooft.portal.housagotchi.mappers;

import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.model.Execution;
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
