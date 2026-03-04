package md.zibliuc.taskmanagerbot.bot;

import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;

public interface Sender {
    void send(OutgoingMessage message);
    void proceededCallbackQuery(String callbackQueryId);
}
