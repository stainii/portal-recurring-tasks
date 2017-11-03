package be.stijnhooft.portal.housagotchi.controllers;

import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.services.RecurringTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recurring-task")
public class RecurringTaskController {

    private final RecurringTaskService recurringTaskService;

    @Inject
    public RecurringTaskController(RecurringTaskService recurringTaskService) {
        this.recurringTaskService = recurringTaskService;
    }

    @RequestMapping("/")
    public List<RecurringTaskDTO> findAll() {
        return recurringTaskService.findAll();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<RecurringTaskDTO> findById(int id) {
        Optional<RecurringTaskDTO> recurringTask = recurringTaskService.findById(id);
        if (recurringTask.isPresent()) {
            return ResponseEntity.ok(recurringTask.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RecurringTaskDTO create(RecurringTaskDTO recurringTask) {
        return recurringTaskService.create(recurringTask);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public RecurringTaskDTO update(RecurringTaskDTO recurringTask, @PathVariable("id") long id) {
        if (id != recurringTask.getId()) {
            throw new IllegalArgumentException("Updating the id is not allowed");
        }
        return recurringTaskService.update(recurringTask);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") long id) {
        recurringTaskService.delete(id);
    }

    @RequestMapping(value = "/{id}/execution", method = RequestMethod.POST)
    public RecurringTaskDTO addExecution(ExecutionDTO execution, @PathVariable("id") long recurringTaskId) {
        return recurringTaskService.addExecution(execution, recurringTaskId);
    }
}
