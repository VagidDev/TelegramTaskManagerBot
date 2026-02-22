package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import org.springframework.stereotype.Component;

@Component
public class TaskManagerBot {
    private final TelegramBot telegramBot;
    private final TelegramUpdateListener updateListener;

    public TaskManagerBot(TelegramBot telegramBot,
                       TelegramUpdateListener updateListener) {
        this.telegramBot = telegramBot;
        this.updateListener = updateListener;
        start();
    }

    public void start() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(updateListener::onUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
