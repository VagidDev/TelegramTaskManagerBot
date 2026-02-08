package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowTasksCommand implements ProceedCommand {
    private static final Logger LOGGER = LogManager.getLogger(ShowTasksCommand.class);

    private final UserService userService;
    private final KeyboardService keyboardService;
    //TODO: need to be tested - no tasks while it was wrote
    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        BotUser botUser = userService.getByChatId(message.chatId());
        if (botUser == null) {
            LOGGER.error("Cannot find user for chat id {}", message.chatId());
            return OutgoingMessage.send(message.chatId(), "У вас нет тасков");
        }

        return OutgoingMessage
                .send(message.chatId(), "Ваши задачи:")
                .keyboard(keyboardService.taskKeyboard(botUser.getTasks()));
    }
}
