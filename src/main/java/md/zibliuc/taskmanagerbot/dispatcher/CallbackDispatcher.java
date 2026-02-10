package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.CallbackDataParser;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(CallbackDispatcher.class);

    private final TelegramSender telegramSender;

    public void dispatch(CallbackQuery callbackQuery) {
        IncomingMessage incomingMessage = new IncomingMessage(
                callbackQuery.maybeInaccessibleMessage().chat().id(),
                callbackQuery.maybeInaccessibleMessage().messageId(),
                callbackQuery.data(),
                null
        );

        LOGGER.info("Received callback data -> {}", incomingMessage);
        telegramSender.proceededCallbackQuery(callbackQuery.id());
        //TODO delegate to necessary CallbackHandler
    }
}
