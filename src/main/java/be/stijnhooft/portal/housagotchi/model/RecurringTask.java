package be.stijnhooft.portal.housagotchi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@ToString
@EqualsAndHashCode
@SequenceGenerator( name = "recurringTaskIdGenerator",
                    sequenceName = "recurring_task_id_sequence")
public class RecurringTask {

    @Id
    @GeneratedValue(generator = "recurringTaskIdGenerator")
    @Getter
    protected Long id;

    @Getter
    @NonNull
    private String name;

    /**
     * The minimum number of days between each execution of this task.
     * It's not necessary to execute this task more regularly.
     **/
    @Getter
    private int minNumberOfDaysBetweenExecutions;

    /**
     * The maximum number of days between each execution of this task.
     * The Housagotchi is going to be very mad when this value gets exceeded.
     **/
    @Getter
    private int maxNumberOfDaysBetweenExecutions;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recurring_task_id")
    private List<Execution> executions;

    public RecurringTask() {
        //necessary for JPA
        //please do not use programmatically

        executions = new ArrayList<>();
    }

    public RecurringTask(@NonNull String name, int minNumberOfDaysBetweenExecutions, int maxNumberOfDaysBetweenExecutions) {
        this();
        checkData(name, minNumberOfDaysBetweenExecutions, maxNumberOfDaysBetweenExecutions);
        this.name = name;
        this.minNumberOfDaysBetweenExecutions = minNumberOfDaysBetweenExecutions;
        this.maxNumberOfDaysBetweenExecutions = maxNumberOfDaysBetweenExecutions;
    }

    public List<Execution> getExecutions() {
        return Collections.unmodifiableList(executions);
    }

    public void addExecution(Execution execution) {
        this.executions.add(execution);
    }


    public void update(@NonNull String name, int minNumberOfDaysBetweenExecutions, int maxNumberOfDaysBetweenExecutions) {
        checkData(name, minNumberOfDaysBetweenExecutions, maxNumberOfDaysBetweenExecutions);
        this.name = name;
        this.minNumberOfDaysBetweenExecutions = minNumberOfDaysBetweenExecutions;
        this.maxNumberOfDaysBetweenExecutions = maxNumberOfDaysBetweenExecutions;
    }

    private void checkData(@NonNull String name, int minNumberOfDaysBetweenExecutions, int maxNumberOfDaysBetweenExecutions) {
        if (minNumberOfDaysBetweenExecutions <= 0
                || maxNumberOfDaysBetweenExecutions <= 0) {
            throw new IllegalArgumentException(String.format("Task %s: The number of days between executions need to be greater than 0. Min: %s, max: %s", name, minNumberOfDaysBetweenExecutions, maxNumberOfDaysBetweenExecutions));
        }
        if (maxNumberOfDaysBetweenExecutions < minNumberOfDaysBetweenExecutions) {
            throw new IllegalArgumentException(String.format("Task %s: The maximum number of days between executions cannot be smaller than the minimum. Min: %s, max: %s", name, minNumberOfDaysBetweenExecutions, maxNumberOfDaysBetweenExecutions));
        }
    }
}
