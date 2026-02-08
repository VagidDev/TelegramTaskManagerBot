package md.zibliuc.taskmanagerbot.dto;

public record TelegramUserData(
        Long chatId,
        String firstName,
        String lastName,
        String username
) {}
