package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements ProceedCommand {
    private final KeyboardService keyboardService;
    private final UserService userService;

    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        return null;
    }
}
