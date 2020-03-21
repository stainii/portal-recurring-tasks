package be.stijnhooft.portal.recurringtasks.messaging;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.recurringtasks.dtos.ExecutionDto;
import be.stijnhooft.portal.recurringtasks.dtos.Source;
import be.stijnhooft.portal.recurringtasks.services.RecurringTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EventTopicListener {

    private final RecurringTaskService recurringTaskService;
    private final String deploymentName;

    @Autowired
    public EventTopicListener(RecurringTaskService recurringTaskService, @Value("${deployment-name}") String deploymentName) {
        this.recurringTaskService = recurringTaskService;
        this.deploymentName = deploymentName;
    }

    @StreamListener(EventTopic.INPUT)
    public void receive(List<Event> events) {
        log.info("Received events: {}", events);

        events.stream()
                .filter(event -> !event.getSource().equals(deploymentName))
                .filter(event -> event.getFlowId().startsWith(deploymentName))
                .filter(event -> event.getFlowAction() == FlowAction.END)
                .forEach(event -> {
                    var execution = new ExecutionDto(event.getPublishDate().toLocalDate(), Source.EVENT);
                    long recurringTaskId = Long.parseLong(event.getFlowId().substring(deploymentName.length() + 1));
                    recurringTaskService.addExecution(execution, recurringTaskId);
                });
    }

}
