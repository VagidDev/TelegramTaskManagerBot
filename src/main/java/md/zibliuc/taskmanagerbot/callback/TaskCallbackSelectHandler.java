package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.TaskService;
import md.zibliuc.taskmanagerbot.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCallbackSelectHandler {
    private static final Logger LOGGER = LogManager.getLogger(TaskCallbackSelectHandler.class);
    private final TaskService taskService;
    private final KeyboardService keyboardService;

    public OutgoingMessage handle(IncomingMessage message) {
        try {
            Long callbackId = message.callbackData().asLong();
            Task task = taskService.get(callbackId);
            if (task != null) {
                return OutgoingMessage
                        .edit(message.chatId(), message.messageId(),
                                "Задание: " + task.getName() + "\n" +
                                        "Выполнить до: " + DateTimeUtil.parseDateTimeToString(task.getDeadline())
                        ).keyboard(keyboardService.crudKeyboard(task.getId()));

            }
            LOGGER.warn("Cannot find task for taskId {}. Callback received {}", callbackId, message.callbackData());
            return OutgoingMessage
                    .send(message.chatId(), "Упс, я не смог найти ваше здание((((");
        } catch (NumberFormatException e) {
            LOGGER.error(
                    "Error while getting callback data from callback {}. Error -> {}",
                    message.callbackData(),
                    e.getLocalizedMessage()
            );
            return OutgoingMessage.send(message.chatId(), "Вы точно туда нажали?????");
        }
    }
}
