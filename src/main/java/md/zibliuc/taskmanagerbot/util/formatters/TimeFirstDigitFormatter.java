package md.zibliuc.taskmanagerbot.util.formatters;

public class TimeFirstDigitFormatter implements Formatter<String> {
    @Override
    public FormatResult<String> format(String s) {
        String[] splitString = s.split(":");
        if (splitString.length != 2) {
            return new FormatResult<>(FormatStatus.FAILED, null);
        }
        if (splitString[0].length() > 2 || splitString[1].length() > 2) {
            return new FormatResult<>(FormatStatus.FAILED, null);
        }

        if (splitString[0].length() == 1) {
            splitString[0] = "0" + splitString[0];
        }

        return new FormatResult<>(FormatStatus.SUCCESS, String.join(splitString[0], splitString[1]));
    }
}
