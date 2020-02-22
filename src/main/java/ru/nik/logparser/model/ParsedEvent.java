package ru.nik.logparser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ParsedEvent {
    private int eventTypeId;
    private Date eventTimestamp;
    private Map<String, String> data;
}
