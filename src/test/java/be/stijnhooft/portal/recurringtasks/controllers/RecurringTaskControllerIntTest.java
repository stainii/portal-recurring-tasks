package be.stijnhooft.portal.recurringtasks.controllers;

import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDTO;
import be.stijnhooft.portal.recurringtasks.dtos.RecurringTaskDto;
import be.stijnhooft.portal.recurringtasks.messaging.EventTopic;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        MockitoTestExecutionListener.class})
public class RecurringTaskControllerIntTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    private RecurringTaskController controller;

    /*
     * Mocks in an integration test?
     * Yes, to avoid having to run an active RabbitMQ instance, which is out of scope of this integration test.
     **/
    @MockBean
    private EventTopic eventTopic;

    @Mock
    private MessageChannel messageChannel;

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-findAll-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findAll() {
        List<RecurringTaskDto> expectedModules = Arrays.asList(
                new RecurringTaskDto(1L, "Dusting", 3, 7, null),
                new RecurringTaskDto(2L, "Watering the plants", 3, 4, LocalDate.of(2017, Month.OCTOBER, 23)));

        List<RecurringTaskDto> recurringTasks = controller.findAll();

        assertEquals(expectedModules, recurringTasks);
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-findAll-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findById() {
        RecurringTaskDto recurringTask = new RecurringTaskDto(2L, "Watering the plants", 3, 4, LocalDate.of(2017, Month.OCTOBER, 23));
        ResponseEntity<RecurringTaskDto> response = controller.findById(2L);
        assertEquals(recurringTask, response.getBody());
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-findAll-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByIdWhenItDoesNotExist() {
        ResponseEntity<RecurringTaskDto> response = controller.findById(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-create-initial.xml")
    @ExpectedDatabase(value = "/datasets/RecurringTaskControllerTest-create-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    public void create() {
        RecurringTaskDto recurringTask = new RecurringTaskDto("Dusting", 3, 7);
        controller.create(recurringTask);
    }

    @Test
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-update-initial.xml")
    @ExpectedDatabase(value = "/datasets/RecurringTaskControllerTest-update-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    public void updateWhenSuccess() {
        RecurringTaskDto recurringTask = new RecurringTaskDto(1L, "Dusting the bedroom", 3, 5, LocalDate.now());
        controller.update(recurringTask, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-update-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void updateWhenRecurrentTaskDoesNotExist() {
        RecurringTaskDto recurringTask = new RecurringTaskDto(100L, "Dusting the bedroom", 3, 5, LocalDate.now());
        controller.update(recurringTask, 100L);
    }

    @Test
    public void updateWhenTryingToUpdateTheId() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Updating the id is not allowed");

        controller.update(new RecurringTaskDto(1L, "a", 1, 1,null), 2L);
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
        doReturn(messageChannel).when(eventTopic).eventTopic();

        controller.addExecution(new ExecutionDTO(LocalDate.of(2017, Month.OCTOBER, 23)), 1L);

        verify(eventTopic, times(2)).eventTopic();
        verify(messageChannel).send(any());
        verifyNoMoreInteractions(eventTopic, messageChannel);
    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup("/datasets/RecurringTaskControllerTest-addExecution-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void addExecutionWhenRecurrentTaskDoesNotExist() {
        controller.addExecution(new ExecutionDTO(LocalDate.now()), 100L);
    }
}