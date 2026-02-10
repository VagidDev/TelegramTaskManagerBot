package md.zibliuc.taskmanagerbot.bot;

import md.zibliuc.taskmanagerbot.callback.CallbackType;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CallbackDataParser {
    private static final Logger LOGGER = LogManager.getLogger(CallbackDataParser.class);

    public CallbackData parse(String callback) throws IllegalStateException, NumberFormatException {
        LOGGER.info("Get string for parsing -> {}", callback);
        String[] splitCallback = callback.split(":");
        if (splitCallback.length != 2) {
            //LO
            // Maybe create exception
            throw new IllegalStateException("Cannot parse callback data: " + callback);
        }
        CallbackType callbackType = CallbackType.valueOf(splitCallback[0]);
        String payload = splitCallback[1];
        return new CallbackData(callbackType, payload);
    }
}
