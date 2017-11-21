package be.stijnhooft.portal.housagotchi.dtos;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class ExecutionDTO {

    @Getter
    @NonNull
    private LocalDateTime date;

}