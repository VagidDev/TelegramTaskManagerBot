package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CallbackResponseConfig;
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

    private final CallbackResponseConfig callbackResponseConfig;
    private final DateTimeUtil dateTimeUtil;
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
                    yield callbackResponseConfig.getActionError();
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
        if (task != null) {
            return callbackResponseConfig
                    .getComplete()
                    .formatted(task.getName());
        }
        LOGGER.error("Cannot complete task with id {}", taskId);
        return callbackResponseConfig.getCompleteError();
    }

    private String onPostpone(Long taskId) {
        // By default, it will postpone on 15 minutes
        Long postponeMinutes = 15L;
        Task postponedTask = taskService.postponeTask(taskId, postponeMinutes);
        if (postponedTask != null) {
            return callbackResponseConfig.getPostpone()
                    .formatted(postponedTask.getName(),
                            dateTimeUtil.parseDateTimeToDateTimeString(postponedTask.getDeadline()));
        }
        LOGGER.error("Cannot postpone task with id {}", taskId);
        return callbackResponseConfig.getPostponeError();
    }

    private String onDelete(Long taskId) {
        LOGGER.info("Trying to delete task with id {}", taskId);
        Task task = taskService.delete(taskId);
        if (task != null) {
            return callbackResponseConfig
                    .getDelete()
                    .formatted(task.getName());
        }
        LOGGER.error("Cannot delete task with id {}", taskId);
        return callbackResponseConfig.getDeleteError();
    }

    private String onEdit(Long id) {
        //not implemented yet
        return null;
    }
}
