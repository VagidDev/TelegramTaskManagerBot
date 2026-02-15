package md.zibliuc.taskmanagerbot.bot;

import md.zibliuc.taskmanagerbot.callback.CallbackType;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CallbackDataParser {
    private static final Logger LOGGER = LogManager.getLogger(CallbackDataParser.class);

    public CallbackData parse(String callback) throws IllegalStateException {
        LOGGER.info("Get string for parsing -> {}", callback);
        String[] splitCallback = callback.split(":");
        if (splitCallback.length != 2) {
            LOGGER.error("Split callback length is {}, but required 2, received callback -> {}",
                    splitCallback.length,
                    callback
            );
            return new CallbackData(CallbackType.UNDEFINED, "");
        }

        try {
            CallbackType callbackType = CallbackType.valueOf(splitCallback[0]);
            String payload = splitCallback[1];
            return new CallbackData(callbackType, payload);
        } catch (IllegalStateException e) {
            LOGGER.error("Cannot parse value of callback {}",
                    callback,
                    e
            );
            return new CallbackData(CallbackType.UNDEFINED, "");
        }
    }
}
