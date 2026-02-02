package md.zibliuc.taskmanagerbot.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tasks")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime deadline;
    @Column(nullable = false)
    private boolean isCompleted = false;
    @Column(nullable = false)
    private boolean sendNotification = true;
    //TODO: change it to lazy and optimize project for this
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
