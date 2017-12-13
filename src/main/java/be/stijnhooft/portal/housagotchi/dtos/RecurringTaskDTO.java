package be.stijnhooft.portal.housagotchi.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
public class RecurringTaskDTO {

    @Getter
    private Long id;

    @Getter
    @NonNull
    private String name;

    /**
     * The minimum number of days between each execution of this task.
     * It's not necessary to execute this task more regularly.
     **/
    @Getter
    @NonNull
    private int minNumberOfDaysBetweenExecutions;

    /**
     * The maximum number of days between each execution of this task.
     * The Housagotchi is going to be very mad when this value gets exceeded.
     **/
    @Getter
    @NonNull
    private int maxNumberOfDaysBetweenExecutions;

    /**
     * The date and time of the last execution of this task.
     * When never executed, this value is null
     **/
    @Getter
    public LocalDateTime lastExecution;

    public RecurringTaskDTO() {}

    public RecurringTaskDTO(String name, int minNumberOfDaysBetweenExecutions, int maxNumberOfDaysBetweenExecutions) {
        this.name = name;
        this.minNumberOfDaysBetweenExecutions = minNumberOfDaysBetweenExecutions;
        this.maxNumberOfDaysBetweenExecutions = maxNumberOfDaysBetweenExecutions;
    }

    public RecurringTaskDTO(Long id, String name, int minNumberOfDaysBetweenExecutions, int maxNumberOfDaysBetweenExecutions, LocalDateTime lastExecution) {
        this(name, minNumberOfDaysBetweenExecutions, maxNumberOfDaysBetweenExecutions);
        this.id = id;
        this.lastExecution = lastExecution;
    }


}
