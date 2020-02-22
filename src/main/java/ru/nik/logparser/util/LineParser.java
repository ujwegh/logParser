package ru.nik.logparser.util;

import ru.nik.logparser.model.EventType;
import ru.nik.logparser.model.ParsedEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser {

    static private Map<String, Pattern> compiledPatterns = Collections.synchronizedMap(new HashMap<>());

    static private Pattern compile(String pattern) {
        Pattern p = compiledPatterns.get(pattern);
        if (p != null) return p;
        p = Pattern.compile(pattern);
        compiledPatterns.put(pattern, p);
        return p;
    }

    static public List<ParsedEvent> parse(String line, EventType[] eventTypes, Date sameDate) {
        List<ParsedEvent> events = new ArrayList<>();
        Date timestamp = sameDate != null ? sameDate : new Date();

        Arrays.stream(eventTypes).forEachOrdered(eventType -> {
            Pattern pattern = compile(eventType.getPattern());
            Matcher matcher = pattern.matcher(line);

            Map<String, String> params;
            if (matcher.find()) {
                params = new HashMap<>();

                eventType.getData().forEach((key, value1) -> {
                    Pattern parameterPattern = compile(value1);
                    Matcher parameterMatcher = parameterPattern.matcher(line);
                    String value = null;
                    if (parameterMatcher.find()) {
                        if (parameterMatcher.groupCount() > 0) {
                            value = parameterMatcher.group(1);
                        } else {
                            value = parameterMatcher.group(0);
                        }
                    }
                    if (value != null) {
                        params.put(key, value);
                    }
                });
                events.add(new ParsedEvent(eventType.getId(), timestamp, params));
            }
        });
        return events;
    }
}
