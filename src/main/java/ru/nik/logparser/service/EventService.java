package ru.nik.logparser.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import ru.nik.logparser.model.EventType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Сервис управления событиями
 * сохраняем/удаляем типы событий в rules.json
 */
@Slf4j
public class EventService {
    static EventType[] events = loadEvents();
    static ReentrantLock locker = new ReentrantLock();

    static private EventType[] loadEvents() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File("rules.json"), EventType[].class);
        } catch (Exception ex) {
            return new EventType[0];
        }
    }

    static public EventType[] listEventTypes() {
        return events;
    }

    static public void addEventType(EventType event) {
        locker.lock();
        try {
            int size = events.length;
            EventType[] newEvents = Arrays.copyOf(events, size + 1);
            newEvents[size] = event;
            events = newEvents;
            writeEvents();
        } catch (Exception ex) {
            log.info("Failed to add new event type: {}", event);
        } finally {
            locker.unlock();
        }
    }

    private static void writeEvents() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("rules.json"), events);
    }

    static public void deleteEventType(int id) {
        locker.lock();
        try {
            int size = events.length;
            EventType[] eventTypes = Arrays.copyOf(events, size);
            EventType eventTypeToRemove = Arrays.stream(eventTypes)
                    .filter(et -> et.id == id).findFirst().orElse(null);
            ArrayUtils.removeElement(eventTypes, eventTypeToRemove);
            events = eventTypes;
            writeEvents();

        } catch (Exception ex) {
            log.info("Failed to delete event type with id: {}", id);
        } finally {
            locker.unlock();
        }
    }


}
