package be.stijnhooft.portal.recurringtasks.controllers;

import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDto;
import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import be.stijnhooft.portal.recurringtasks.services.RecurringTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recurring-task")
public class RecurringTaskController {

    private final RecurringTaskService recurringTaskService;

    @Autowired
    public RecurringTaskController(RecurringTaskService recurringTaskService) {
        this.recurringTaskService = recurringTaskService;
    }

    @RequestMapping("/")
    public List<RecurringTaskDto> findAll() {
        return recurringTaskService.findAll();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<RecurringTaskDto> findById(@PathVariable("id") Long id) {
        Optional<RecurringTaskDto> recurringTask = recurringTaskService.findById(id);
        return recurringTask
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RecurringTaskDto create(@RequestBody RecurringTaskDto recurringTask) {
        return recurringTaskService.create(recurringTask);
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.PUT)
    public RecurringTaskDto update(@RequestBody RecurringTaskDto recurringTask, @PathVariable( "id") Long id) {
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
    public RecurringTaskDto addExecution(@RequestBody ExecutionDto execution, @PathVariable("id") Long recurringTaskId) {
        return recurringTaskService.addExecution(execution, recurringTaskId);
    }
}
