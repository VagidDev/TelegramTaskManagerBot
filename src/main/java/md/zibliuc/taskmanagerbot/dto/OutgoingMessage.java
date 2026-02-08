package md.zibliuc.taskmanagerbot.dto;

import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.Getter;

@Getter
public class OutgoingMessage {
    private Long chatId;
    private String text;
    private Integer messageId;
    //TODO: create logic that will avoid using telegrambot.api
    private Keyboard keyboard;
    private MessageAction action;

    private static OutgoingMessage base(Long chatId, String text, MessageAction action) {
        OutgoingMessage message = new OutgoingMessage();
        message.chatId = chatId;
        message.text = text;
        message.action = action;
        return message;
    }

    public static OutgoingMessage send(Long chatId, String text) {
        return base(chatId, text, MessageAction.SEND);
    }

    public static OutgoingMessage edit(Long chatId, Integer messageId, String text) {
        OutgoingMessage message = base(chatId, text, MessageAction.EDIT);
        message.messageId = messageId;
        return message;
    }

    public static OutgoingMessage delete(Long chatId, Integer messageId) {
        OutgoingMessage message = new OutgoingMessage();
        message.chatId = chatId;
        message.messageId = messageId;
        message.action = MessageAction.DELETE;
        return message;
    }

    public OutgoingMessage keyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
        return this;
    }

}
