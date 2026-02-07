package md.zibliuc.taskmanagerbot.command;

import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;

public interface ProceedCommand {
    OutgoingMessage proceed(Long chatId, String text);
}
