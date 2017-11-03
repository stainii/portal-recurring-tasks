package be.stijnhooft.portal.housagotchi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator( name = "executionIdGenerator",
                    sequenceName = "execution_id_sequence")
@EqualsAndHashCode
@ToString
public class Execution {

    @Id
    @GeneratedValue(generator = "executionIdGenerator")
    @Getter
    @NonNull
    private long id;

    @Getter
    @NonNull
    private LocalDateTime date;

    /** The name of the person who has executed the recurring task **/
    @Getter
    @NonNull
    private String executor;

    public Execution() {
    }

    public Execution(LocalDateTime date, String executor) {
        this.date = date;
        this.executor = executor;
    }
}