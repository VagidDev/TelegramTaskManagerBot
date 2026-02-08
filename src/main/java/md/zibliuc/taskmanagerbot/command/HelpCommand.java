package md.zibliuc.taskmanagerbot.command;

import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements ProceedCommand {
    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        return OutgoingMessage.send(message.chatId(), "Доступные команды: /start, /help");
    }
}
