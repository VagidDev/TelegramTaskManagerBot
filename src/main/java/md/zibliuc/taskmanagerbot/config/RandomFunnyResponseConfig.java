package md.zibliuc.taskmanagerbot.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Random;

@ConfigurationProperties("ui.responses.unexpected")
@RequiredArgsConstructor
public class RandomFunnyResponseConfig {
    private final static Random RANDOMIZER = new Random();
    private final List<String> randomFunnyResponseList;

    private int range = 0;

    @PostConstruct
    void init() {
        range = randomFunnyResponseList.size() - 1;
    }

    public String getFunnyResponse() {
        if (range < 1) {
            return "Я тебя не понимаю(";
        }

        int randomIndex = RANDOMIZER.nextInt(range);
        return randomFunnyResponseList.get(randomIndex);
    }
}
