package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.conversation.ConversationContext;
import md.zibliuc.taskmanagerbot.conversation.ConversationState;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCallbackDateHandler {
    private final UserConversationStateService conversationStateService;

    public OutgoingMessage handle(IncomingMessage incomingMessage) {
        //TODO: handle choice of task
        ConversationContext ctx = conversationStateService.get(incomingMessage.chatId());
        CallbackData data = incomingMessage.callbackData();
        if (ctx.getState() == ConversationState.WAITING_DATE) {
            ctx.setDate(data.asDate());
            ctx.setState(ConversationState.WAITING_TIME);
            return OutgoingMessage.edit(
                    incomingMessage.chatId(),
                    incomingMessage.messageId(),
                    "Введите время (HH:mm)"
            );
        }
        conversationStateService.reset(incomingMessage.chatId());
        return OutgoingMessage.send(incomingMessage.chatId(), "Не понятно что ты хочешь от меня...");
    }
}
