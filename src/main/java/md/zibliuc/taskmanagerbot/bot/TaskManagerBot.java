package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import md.zibliuc.taskmanagerbot.handler.MessageHandler;
import org.springframework.stereotype.Component;

@Component
public class TaskManagerBot {
    private final TelegramBot telegramBot;
    private final MessageHandler messageHandler;

    public TaskManagerBot(TelegramBot telegramBot,
                       MessageHandler messageHandler) {
        this.telegramBot = telegramBot;
        this.messageHandler = messageHandler;
        start();
    }

    private void start() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(messageHandler::handle);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
