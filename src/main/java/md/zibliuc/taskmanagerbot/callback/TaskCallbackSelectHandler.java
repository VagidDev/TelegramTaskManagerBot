package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.TaskManagerApplication;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCallbackSelectHandler {
    private static final Logger LOGGER = LogManager.getLogger(TaskCallbackSelectHandler.class);
    private final TaskService taskService;

    public OutgoingMessage handle(IncomingMessage message) {
        Task task = taskService.get(message.callbackData().asLong());
        return OutgoingMessage
                .edit(message.chatId(), message.messageId(), "Задание: ");
    }
}
