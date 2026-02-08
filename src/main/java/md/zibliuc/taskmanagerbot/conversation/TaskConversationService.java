package md.zibliuc.taskmanagerbot.conversation;

import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TaskConversationService {
    private static final Logger LOGGER = LogManager.getLogger(TaskConversationService.class);
    public void handle(IncomingMessage message) {
        LOGGER.info("Received message {}", message);
    }

    public void onTitle(Long chatId, String text) {}
    public void onDate(Long chatId, LocalDate date) {}
    public void onTime(Long chatId, LocalTime time) {}

}
