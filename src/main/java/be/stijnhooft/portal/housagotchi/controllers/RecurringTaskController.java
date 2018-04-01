package be.stijnhooft.portal.housagotchi.controllers;

import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import be.stijnhooft.portal.housagotchi.services.RecurringTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<RecurringTaskDTO> findById(@PathVariable("id") Long id) {
        Optional<RecurringTaskDTO> recurringTask = recurringTaskService.findById(id);
        if (recurringTask.isPresent()) {
            return ResponseEntity.ok(recurringTask.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RecurringTaskDTO create(@RequestBody RecurringTaskDTO recurringTask) {
        return recurringTaskService.create(recurringTask);
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.PUT)
    public RecurringTaskDTO update(@RequestBody RecurringTaskDTO recurringTask, @PathVariable( "id") Long id) {
        if (!id.equals(recurringTask.getId())) {
            throw new IllegalArgumentException("Updating the id is not allowed");
        }
        return recurringTaskService.update(recurringTask);
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        recurringTaskService.delete(id);
    }

    @RequestMapping(value = "/{id}/execution/", method = RequestMethod.POST)
    public RecurringTaskDTO addExecution(@RequestBody ExecutionDTO execution, @PathVariable("id") Long recurringTaskId) {
        return recurringTaskService.addExecution(execution, recurringTaskId);
    }
}
