package is.nord.controller;

import is.nord.FlashMessage;
import is.nord.model.Event;
import is.nord.model.User;
import is.nord.repository.EventRepository;
import is.nord.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    private EventRepository eventRepository;

    @RequestMapping("/")
    public String listAllEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "index";
    }

    @RequestMapping("/getAllEvents")
    @ResponseBody
    public List<Event> getAllEvents() {
        List<Event> rides = eventRepository.findAllByOrderByTime();
        return rides;
        //return eventService.findAll();
    }





    @RequestMapping("/event/add")
    public String formNewEvent(Model model) {

        if(!model.containsAttribute("event")) {
            model.addAttribute("event", new Event());
        }

        // Add model attributes needed for new form
        //model.addAttribute("event", new Event());
        model.addAttribute("action", "/event/save");
        model.addAttribute("heading","Nýr viðburður");
        model.addAttribute("submit","Birta viðburð");

        return "eventForm";
    }


    @RequestMapping("/event/{id}/edit")
    public String formEditEvent(@PathVariable Long eventId, Model model) {

        if(!model.containsAttribute("event")) {
            model.addAttribute("event", eventService.findOne(eventId));
        }

        //model.addAttribute("event", newsService.findOne(newsId));
        model.addAttribute("action", String.format("/event/%s", eventId));
        model.addAttribute("heading", "Breyta viðburði");
        model.addAttribute("submit","Uppfæra viðburð");

        return "eventForm";
    }

    @RequestMapping(value = "/event/save", method = RequestMethod.POST)
    public String saveEvent(@Valid Event event, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", result);

            // Add ad if invalid was received
            redirectAttributes.addFlashAttribute("event", event);

            // Redirect back to the form
            return "redirect:/event/add";
        }

        // Save to database through newsService
        eventService.save(event);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Tókst að bæta við viðburði", FlashMessage.Status.SUCCESS));

        // Redirect browser to /
        return "redirect:/";
    }

    @RequestMapping(value = "/event/{id}", method = RequestMethod.POST)
    public String updateEvent(@Valid Event event, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", result);

            // Add data if invalid was received
            redirectAttributes.addFlashAttribute("event", event);

            // Redirect back to the form
            return String.format("redirect:/event/%s/edit",event.getId());
        }

        eventService.save(event);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Tókst að uppfæra viðburð", FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    @RequestMapping("/event/{id}/delete")
    public String deleteEvent(@PathVariable Long newsId, RedirectAttributes redirectAttributes) {
        Event event = eventService.findOne(newsId);
        eventService.delete(event);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Viðburði hefur verið eytt!", FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }
}
