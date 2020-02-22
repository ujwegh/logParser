package ru.nik.logparser.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventType {
    public int id;
    public String pattern;
    public Map<String, String> data;
}
