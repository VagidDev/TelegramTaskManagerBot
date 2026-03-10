package md.zibliuc.taskmanagerbot.dto;

public record IncomingTextMessage(
        Long chatId,
        Integer messageId,
        String text,
        TelegramUserData userData
) implements IncomingMessage {
}
