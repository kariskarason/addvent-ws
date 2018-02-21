package is.nord.service;

import is.nord.model.Event;
import is.nord.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    @Transactional
    public Iterable<Event> findAll() {
        return eventRepository.findAllByOrderByTime();
    }

    @Override
    @Transactional
    public Event findOne(Long id) {
        return eventRepository.findOne(id);
    }

    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }
}
