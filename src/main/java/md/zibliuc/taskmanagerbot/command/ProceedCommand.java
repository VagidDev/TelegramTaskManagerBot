package md.zibliuc.taskmanagerbot.command;

import md.zibliuc.taskmanagerbot.dto.IncomingTextMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;

public interface ProceedCommand {
    OutgoingMessage proceed(IncomingTextMessage incomingTextMessage);
}
