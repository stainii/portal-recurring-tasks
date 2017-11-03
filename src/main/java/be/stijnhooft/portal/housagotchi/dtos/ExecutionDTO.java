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

    /** The name of the person who has executed the recurring task **/
    @Getter
    @NonNull
    private String executor;

}