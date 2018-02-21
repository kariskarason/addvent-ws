package is.nord.config;

import is.nord.model.Event;
import is.nord.repository.EventRepository;
import is.nord.model.User;
import is.nord.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseLoader implements ApplicationRunner{
    private final EventRepository events;
    private final UserRepository users;

    @Autowired
    public DatabaseLoader(EventRepository events, UserRepository users) {
        this.events = events;
        this.users = users;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Event event = new Event("Vísindaferð í Origo", "Borgartún 21", "Origo", "Pepp",
                LocalDateTime.now());
        events.save(event);

        event = new Event("Vetrarhátíð", "Arendell", "Nörd", "woop woop",
                LocalDateTime.of(2014, 1, 1, 10, 10, 30));
        events.save(event);

        event = new Event("Sumarhátíð", "Klambratún", "Nörd", "sumarylur",
                LocalDateTime.of(2014, 5, 1, 10, 10, 30));
        events.save(event);

        users.save(new User("nordadmin", "password", "ROLE_ADMIN"));
    }
}
