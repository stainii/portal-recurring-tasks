package be.stijnhooft.portal.recurringtasks.dtos;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionDto {

    @Getter
    @NonNull
    private LocalDate date;

    @Getter
    @NonNull
    private Source source;

}