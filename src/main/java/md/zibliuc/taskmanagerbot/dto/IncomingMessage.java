package md.zibliuc.taskmanagerbot.dto;

public record IncomingMessage (
    Long chatId,
    Integer messageId,
    String text,
    CallbackData callbackData,
    TelegramUserData userData
)
{ }
