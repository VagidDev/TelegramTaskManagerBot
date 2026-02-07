package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.handler.MessageHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskManagerBot {
    private final TelegramBot telegramBot;
    private final TelegramUpdateListener updateListener;

    public void start() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(updateListener::onUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }


//Old logic
//private final MessageHandler messageHandler;
//    public TaskManagerBot(TelegramBot telegramBot,
//                       MessageHandler messageHandler) {
//        this.telegramBot = telegramBot;
//        this.messageHandler = messageHandler;
//        start();
//    }
//
//    private void start() {
//        telegramBot.setUpdatesListener(updates -> {
//            updates.forEach(messageHandler::handle);
//            return UpdatesListener.CONFIRMED_UPDATES_ALL;
//        });
//    }
}
