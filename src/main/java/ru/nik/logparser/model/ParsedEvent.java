package ru.nik.logparser.model;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
public class ParsedEvent {
    public int eventTypeId;
    public Date eventTimestamp;
    public Map<String, String> data;
}
