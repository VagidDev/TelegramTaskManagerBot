package md.zibliuc.taskmanagerbot.dto;

import com.pengrad.telegrambot.model.MessageId;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;

public class OutgoingMessage {
    private Long chatId;
    private String text;
    private Integer messageId;
    private KeyboardType keyboardType;
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

    public OutgoingMessage keyboard(KeyboardType keyboardType) {
        this.keyboardType = keyboardType;
        return this;
    }

}
