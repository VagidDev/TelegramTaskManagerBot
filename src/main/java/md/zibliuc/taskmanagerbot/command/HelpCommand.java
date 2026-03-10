package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CommandResponseConfig;
import md.zibliuc.taskmanagerbot.dto.IncomingTextMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements ProceedCommand {
    private final CommandResponseConfig commandResponseConfig;

    @Override
    public OutgoingMessage proceed(IncomingTextMessage incomingTextMessage) {
        return OutgoingMessage.send(incomingTextMessage.chatId(), commandResponseConfig.getHelp());
    }
}
