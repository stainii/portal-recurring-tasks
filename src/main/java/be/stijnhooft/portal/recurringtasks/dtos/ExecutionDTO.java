package be.stijnhooft.portal.recurringtasks.dtos;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionDTO {

    @Getter
    @NonNull
    private LocalDate date;

}