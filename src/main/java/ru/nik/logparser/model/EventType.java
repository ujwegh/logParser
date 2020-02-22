package ru.nik.logparser.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventType {
    private int id;
    private String pattern;
    private Map<String, String> data;
}
