package md.zibliuc.taskmanagerbot.util.formatters;

import md.zibliuc.taskmanagerbot.conversation.TaskConversationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class TimeFirstDigitFormatter implements Formatter<String> {
    private static final Logger LOGGER = LogManager.getLogger(TimeFirstDigitFormatter.class);

    @Override
    public FormatResult<String> format(String s) {
        String[] splitString = s.split(":");
        LOGGER.debug("Split string to array: {}", Arrays.toString(splitString));
        if (splitString.length != 2) {
            return new FormatResult<>(FormatStatus.FAILED, null);
        }
        if (splitString[0].length() > 2 || splitString[1].length() > 2) {
            return new FormatResult<>(FormatStatus.FAILED, null);
        }

        if (splitString[0].length() == 1) {
            splitString[0] = "0" + splitString[0];
        }

        return new FormatResult<>(FormatStatus.SUCCESS, String.join(":", splitString[0], splitString[1]));
    }
}
