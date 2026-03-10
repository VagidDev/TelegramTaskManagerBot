package md.zibliuc.taskmanagerbot.mapper;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.CallbackDataParser;
import md.zibliuc.taskmanagerbot.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateMapper {
    private static final Logger LOGGER = LogManager.getLogger(UpdateMapper.class);

    private final CallbackDataParser callbackDataParser;

    public IncomingMessage map(Update update) {
        if (update.message() != null && update.message().text() != null) {
            return buildTextMessage(update.message());
        } else if (update.callbackQuery() != null) {
            return buildCallbackMessage(update.callbackQuery());
        } else {
            LOGGER.warn("Undefined type of message -> {}", update);
            return null;
        }
    }

    private TelegramUserData getUserData(User user) {
        return new TelegramUserData(
                user.firstName(),
                user.lastName(),
                user.username()
        );
    }

    private IncomingTextMessage buildTextMessage(Message message) {
        LOGGER.debug("Building text message");

        TelegramUserData telegramUserData = getUserData(message.from());
        return new IncomingTextMessage(
                message.chat().id(),
                message.messageId(),
                message.text(),
                telegramUserData
        );
    }

    private IncomingCallbackMessage buildCallbackMessage(CallbackQuery callbackQuery) {
        LOGGER.debug("Building callback message");

        TelegramUserData telegramUserData = getUserData(callbackQuery.from());
        CallbackData callbackData = callbackDataParser.parse(callbackQuery.data());
        return new IncomingCallbackMessage(
                callbackQuery.maybeInaccessibleMessage().chat().id(),
                callbackQuery.maybeInaccessibleMessage().messageId(),
                callbackQuery.data(),
                telegramUserData,
                callbackQuery.id(),
                callbackData
        );
    }
}
