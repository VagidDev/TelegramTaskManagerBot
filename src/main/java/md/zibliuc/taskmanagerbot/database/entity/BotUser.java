package md.zibliuc.taskmanagerbot.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class BotUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;
    //TODO: change it to lazy and optimize project for this
    @OneToMany(mappedBy = "botUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Task> tasks;

    public List<Task> getUncompletedTask() {
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .toList();
    }

}
