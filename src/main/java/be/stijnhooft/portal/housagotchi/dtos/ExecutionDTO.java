package be.stijnhooft.portal.housagotchi.dtos;

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