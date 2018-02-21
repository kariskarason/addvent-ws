package is.nord.repository;

import is.nord.model.Event;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    // Returns the events ordered by time
    //@RestResource(rel = "all-events-in-order", path = "allEvents")
    List<Event> findAllByOrderByTime();

    // Allow users to search titles og events
    //@RestResource(rel = "title-contains", path = "containsTitle")
    //List<Event> findByTitleContaining(@Param("title") String title);

}