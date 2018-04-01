package be.stijnhooft.portal.housagotchi.controllers;

import be.stijnhooft.portal.housagotchi.PortalHousagotchiApplication;
import be.stijnhooft.portal.housagotchi.dtos.ExecutionDTO;
import be.stijnhooft.portal.housagotchi.dtos.RecurringTaskDTO;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PortalHousagotchiApplication.class)
@ActiveProfiles("local")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class RecurringTaskControllerIntTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    private RecurringTaskController controller;

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-findAll-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findAll() {
        List<RecurringTaskDTO> expectedModules = Arrays.asList(
                new RecurringTaskDTO(Long.valueOf(1), "Dusting", 3, 7, null),
                new RecurringTaskDTO(Long.valueOf(2), "Watering the plants", 3, 4, LocalDate.of(2017, Month.OCTOBER, 23)));

        List<RecurringTaskDTO> recurringTasks = controller.findAll();

        assertEquals(expectedModules, recurringTasks);
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-findAll-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findById() {
        RecurringTaskDTO recurringTask = new RecurringTaskDTO(Long.valueOf(2), "Watering the plants", 3, 4, LocalDate.of(2017, Month.OCTOBER, 23));
        ResponseEntity<RecurringTaskDTO> response = controller.findById(2L);
        assertEquals(recurringTask, response.getBody());
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-findAll-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByIdWhenItDoesNotExist() {
        ResponseEntity<RecurringTaskDTO> response = controller.findById(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-create-initial.xml")
    @ExpectedDatabase(value = "/datasets/RecurringTaskControllerTest-create-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    public void create() {
        RecurringTaskDTO recurringTask = new RecurringTaskDTO("Dusting", 3, 7);
        controller.create(recurringTask);
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-update-initial.xml")
    @ExpectedDatabase(value = "/datasets/RecurringTaskControllerTest-update-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    public void updateWhenSuccess() {
        RecurringTaskDTO recurringTask = new RecurringTaskDTO(1L, "Dusting the bedroom", 3, 5, LocalDate.now());
        controller.update(recurringTask, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-update-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void updateWhenRecurrentTaskDoesNotExist() {
        RecurringTaskDTO recurringTask = new RecurringTaskDTO(100L, "Dusting the bedroom", 3, 5, LocalDate.now());
        controller.update(recurringTask, 100L);
    }

    @Test
    public void updateWhenTryingToUpdateTheId() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Updating the id is not allowed");

        controller.update(new RecurringTaskDTO(1L, "a", 1, 1,null), 2L);
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-delete-initial.xml")
    @ExpectedDatabase(value = "/datasets/RecurringTaskControllerTest-delete-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    public void delete() {
        controller.delete(2L);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-delete-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void deleteWhenRecurrentTaskDoesNotExist() {
        controller.delete(100L);
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-addExecution-initial.xml")
    @ExpectedDatabase(value = "/datasets/RecurringTaskControllerTest-addExecution-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    public void addExecutionWhenSuccess() {
        controller.addExecution(new ExecutionDTO(LocalDate.of(2017, Month.OCTOBER, 23)), 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-addExecution-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void addExecutionWhenRecurrentTaskDoesNotExist() {
        controller.addExecution(new ExecutionDTO(LocalDate.now()), 100L);
    }
}