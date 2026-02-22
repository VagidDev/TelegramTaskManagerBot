package md.zibliuc.taskmanagerbot.service;

import md.zibliuc.taskmanagerbot.conversation.TaskConversationService;
import md.zibliuc.taskmanagerbot.validators.SimpleValidator;
import md.zibliuc.taskmanagerbot.validators.TimeFormatSimpleValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeValidationService {
    private static final Logger LOGGER = LogManager.getLogger(TimeValidationService.class);
    //For scalable logic (maybe unnecessary here)
    private final static List<SimpleValidator<String>> TIME_VALIDATORS = List.of(
            new TimeFormatSimpleValidator()
    );

    public boolean validate(String time) {
        for (SimpleValidator<String> simpleValidator : TIME_VALIDATORS) {
            if (!simpleValidator.validate(time)) {
                LOGGER.info("Time string `{}` is invalid. Invalid state was found by class {}",
                        time, simpleValidator.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }
}
