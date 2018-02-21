package is.nord.service;

import is.nord.model.Event;

import java.util.List;

public interface EventService {

    Iterable<Event> findAll();
    Event findOne(Long id);
    void save(Event event);
    void delete(Event event);
}
