package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
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

    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        userService.ensureUserExists(message.userData());
        return OutgoingMessage
                .send(message.chatId(), "Привет! Я Task Manager бот, чем могу быть полезен?")
                .keyboard(keyboardService.menuKeyboard());
    }
}
