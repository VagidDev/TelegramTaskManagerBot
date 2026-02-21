package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.TaskService;
import md.zibliuc.taskmanagerbot.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCallbackActionHandler {
    private static final Logger LOGGER = LogManager.getLogger(TaskCallbackActionHandler.class);

    private final TaskService taskService;
//    private final
    //TODO COMPLETE / DELETE / POSTPONE / EDIT
    //TODO Working only with id
    public OutgoingMessage handle(IncomingMessage incomingMessage) {
        CallbackData callbackData = incomingMessage.callbackData();
        try {
            Long taskId = callbackData.asLong();
            String response = switch (callbackData.type()) {
                case COMPLETE -> onComplete(taskId);
                case DELETE -> onDelete(taskId);
                case POSTPONE -> onPostpone(taskId);
                default -> {
                    //by logic, it should not be used in any conditions due to switch-case in CallbackDispatcher
                    LOGGER.warn("Undefined callback type for task action. Callback received -> {}", callbackData);
                    yield "Шо ты хочешь от этого таска?";
                }
            };

            return OutgoingMessage.send(incomingMessage.chatId(), response);
        } catch (NumberFormatException e) {
            LOGGER.error(
                    "Cannot parse value of callback as Long. Callback received -> {}, Error -> {}",
                    callbackData,
                    e.getLocalizedMessage()
            );
            return null;
        }
    }

    private String onComplete(Long taskId) {
        LOGGER.info("Completing task with id {}", taskId);
        Task task = taskService.completeTask(taskId);
        if (task != null)
            return "Вы выполнили задачу -> " + task.getName();

        LOGGER.error("Cannot complete task with id {}", taskId);
        return "Вы не можете выполнить несуществующую задачу! ";
    }

    private String onPostpone(Long taskId) {
        // By default, it will postpone on 15 minutes
        Long postponeMinutes = 15L;
        Task postponedTask = taskService.postponeTask(taskId, postponeMinutes);
        if (postponedTask != null) {
            return "Вы перенесли задачу `%s` на %s"
                    .formatted(postponedTask.getName(),
                            DateTimeUtil.parseDateTimeToString(postponedTask.getDeadline()));
        }
        LOGGER.error("Cannot postpone task with id {}", taskId);
        return "Не получилось перенести задачу((\nДелай сейчас)";
    }

    private String onDelete(Long taskId) {
        LOGGER.info("Trying to delete task with id {}", taskId);
        Task task = taskService.delete(taskId);
        if (task != null) {
            return "Вы удалили задачу -> " + task.getName();
        }
        LOGGER.error("Cannot delete task with id {}", taskId);
        return "Вы не можее уддалить несуществующую задачу!";
    }

    private String onEdit(Long id) {
        //not implemented yet
        return null;
    }
}
