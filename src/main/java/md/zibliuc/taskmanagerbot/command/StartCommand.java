package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CommandResponseConfig;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements ProceedCommand {
    private final KeyboardService keyboardService;
    private final UserService userService;
    private final CommandResponseConfig commandResponseConfig;

    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        userService.ensureUserExists(message.userData());
        return OutgoingMessage
                .send(message.chatId(), commandResponseConfig.getStart())
                .keyboard(keyboardService.menuKeyboard());
    }
}
