package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CallbackResponseConfig;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.IncomingCallbackMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.KeyboardService;
import md.zibliuc.taskmanagerbot.service.TaskService;
import md.zibliuc.taskmanagerbot.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCallbackSelectHandler {
    private static final Logger LOGGER = LogManager.getLogger(TaskCallbackSelectHandler.class);

    private final CallbackResponseConfig callbackResponseConfig;
    private final DateTimeUtil dateTimeUtil;
    private final KeyboardService keyboardService;
    private final TaskService taskService;

    public OutgoingMessage handle(IncomingCallbackMessage incomingCallbackMessage) {
        try {
            Long callbackId = incomingCallbackMessage.callbackData().asLong();
            Task task = taskService.get(callbackId);
            if (task != null) {
                String response = callbackResponseConfig
                        .getSelectTask()
                        .formatted(task.getName(), dateTimeUtil.parseDateTimeToDateTimeString(task.getDeadline()));
                return OutgoingMessage
                        .edit(incomingCallbackMessage.chatId(), incomingCallbackMessage.messageId(), response)
                        .keyboard(keyboardService.crudKeyboard(task.getId()));
            }
            LOGGER.warn("Cannot find task for taskId {}. Callback received {}", callbackId, incomingCallbackMessage.callbackData());
            return OutgoingMessage
                    .send(incomingCallbackMessage.chatId(), callbackResponseConfig.getSelectTaskNotFound());
        } catch (NumberFormatException e) {
            LOGGER.error(
                    "Error while getting callback data from callback {}. Error -> {}",
                    incomingCallbackMessage.callbackData(),
                    e.getLocalizedMessage()
            );
            return OutgoingMessage.send(incomingCallbackMessage.chatId(), callbackResponseConfig.getSelectTaskError());
        }
    }
}
