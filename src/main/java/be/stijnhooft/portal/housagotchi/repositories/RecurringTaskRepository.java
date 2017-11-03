package be.stijnhooft.portal.housagotchi.repositories;

import be.stijnhooft.portal.housagotchi.model.RecurringTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringTaskRepository extends JpaRepository<RecurringTask, Long> {

}
