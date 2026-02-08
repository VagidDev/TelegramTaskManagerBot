package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowUncompletedTasksCommand implements ProceedCommand {
    private static final Logger LOGGER = LogManager.getLogger(ShowUncompletedTasksCommand.class);

    private final UserService userService;
    private final KeyboardService keyboardService;
    //TODO: need to be tested - no tasks while it was wrote
    //TODO: extract similar logic with ShowTasksCommand
    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        BotUser botUser = userService.getByChatId(message.chatId());
        if (botUser == null) {
            LOGGER.error("Cannot find user for chat id {}", message.chatId());
            return OutgoingMessage.send(message.chatId(), "У вас нет тасков");
        }

        List<Task> taskList = botUser.getUncompletedTask();
        if (taskList.isEmpty()) {
            return OutgoingMessage
                    .send(message.chatId(), "У вас нету не выполненных заданий!");
        }

        return OutgoingMessage
                .send(message.chatId(), "Что будем делать?")
                .keyboard(keyboardService.taskKeyboard(taskList));
    }
}
