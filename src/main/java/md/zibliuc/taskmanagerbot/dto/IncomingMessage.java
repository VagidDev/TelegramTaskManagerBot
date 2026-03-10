package md.zibliuc.taskmanagerbot.dto;

public interface IncomingMessage {
    Long chatId();
    Integer messageId();
    String text();
    TelegramUserData userData();
}
