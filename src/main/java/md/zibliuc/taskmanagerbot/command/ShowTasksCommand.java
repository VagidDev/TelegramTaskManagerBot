package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CommandResponseConfig;
import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowTasksCommand implements ProceedCommand {
    private static final Logger LOGGER = LogManager.getLogger(ShowTasksCommand.class);

    private final UserService userService;
    private final KeyboardService keyboardService;
    private final CommandResponseConfig commandResponseConfig;
    //TODO: need to be tested - no tasks while it was wrote
    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        BotUser botUser = userService.getByChatId(message.chatId());
        if (botUser == null) {
            LOGGER.error("Cannot find user for chat id {}", message.chatId());
            return OutgoingMessage.send(message.chatId(), commandResponseConfig.getShowTasksError());
        }

        List<Task> taskList = botUser.getTasks();
        if (taskList.isEmpty()) {
            return OutgoingMessage
                    .send(message.chatId(), commandResponseConfig.getShowTasksEmpty());
        }

        return OutgoingMessage
                .send(message.chatId(), commandResponseConfig.getShowTasks())
                .keyboard(keyboardService.taskKeyboard(taskList));
    }
}
