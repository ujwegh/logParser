package ru.nik.logparser.util;

import org.junit.jupiter.api.Test;
import ru.nik.logparser.model.EventType;
import ru.nik.logparser.model.ParsedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LineParserTest {

    @Test
    public void noMatch() {
        Map<String, String> params = new HashMap<>();
        EventType[] eventTypes = new EventType[]{new EventType(1, "test", params)};
        List<ParsedEvent> events = LineParser.parse("some other string", eventTypes, null);
        assertEquals(0, events.size());
    }

    @Test
    public void oneMatch() {
        Map<String, String> params = new HashMap<>();
        params.put("x1", "x1=(\\S*)");
        params.put("x2", "x2\\s*=\\s*\\'([^\\']*)\\'");
        EventType[] eventTypes = new EventType[]{new EventType(1, "test", params)};
        List<ParsedEvent> events = LineParser.parse("some string test x1=12 x2 = 'long text' some string", eventTypes, null);
        assertEquals(1, events.size());
        ParsedEvent event = events.get(0);
        assertEquals(1, event.eventTypeId);
        assertNotNull(event.eventTimestamp);
        assertEquals(2, event.data.size());
        assertEquals("12", event.data.get("x1"));
        assertEquals("long text", event.data.get("x2"));
    }

    @Test
    public void twoMatches() {
        Map<String, String> params1 = new HashMap<>();
        params1.put("x1", "x1=(\\S*)");
        params1.put("x2", "x2\\s*=\\s*\\'([^\\']*)\\'");
        Map<String, String> params2 = new HashMap<>();
        params2.put("x1", "x1=(\\S*)");
        EventType[] eventTypes = new EventType[]{
                new EventType(1, "test", params1),
                new EventType(2, "string", params2)};
        List<ParsedEvent> events = LineParser.parse("some string test x1=12 x2 = 'long text' some string", eventTypes, null);
        assertEquals(2, events.size());
        ParsedEvent event = events.get(0);
        assertEquals(1, event.eventTypeId);
        assertNotNull(event.eventTimestamp);
        assertEquals(2, event.data.size());
        assertEquals("12", event.data.get("x1"));
        assertEquals("long text", event.data.get("x2"));
        event = events.get(1);
        assertEquals(2, event.eventTypeId);
        assertNotNull(event.eventTimestamp);
        assertEquals(1, event.data.size());
        assertEquals("12", event.data.get("x1"));
    }
}