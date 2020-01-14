package be.stijnhooft.portal.recurringtasks.repositories;

import be.stijnhooft.portal.recurringtasks.model.RecurringTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringTaskRepository extends JpaRepository<RecurringTask, Long> {

}
