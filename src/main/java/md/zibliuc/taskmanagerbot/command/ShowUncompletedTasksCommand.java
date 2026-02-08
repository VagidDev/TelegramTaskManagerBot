package md.zibliuc.taskmanagerbot.command;

import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.springframework.stereotype.Component;

@Component
public class ShowUncompletedTasksCommand implements ProceedCommand {
    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        return null;
    }
}
