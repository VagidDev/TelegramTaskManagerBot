package md.zibliuc.taskmanagerbot.dto;

public record IncomingCallbackMessage(
        Long chatId,
        Integer messageId,
        String text,
        TelegramUserData userData,
        String callbackId,
        CallbackData callbackData
) implements IncomingMessage {
}
