package ru.nik.logparser.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.nik.logparser.model.EventType;
import ru.nik.logparser.service.EventService;

@Slf4j
@RestController
@RequestMapping(path = "/events")
public class ApiController {

    @GetMapping
    public EventType[] list() {
        log.info("Get all event types");
        return EventService.listEventTypes();
    }

    @PostMapping
    public void add(@RequestBody EventType event) {
        log.info("Add new event type: {}", event);
        EventService.addEventType(event);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("Delete event type with id: {}", id);
        EventService.deleteEventType(id);
    }
}
