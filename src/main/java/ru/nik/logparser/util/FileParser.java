package ru.nik.logparser.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.nik.logparser.model.EventType;
import ru.nik.logparser.model.ParsedEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FileParser {
    static public Boolean parse(String inFolder, String outFolder, String fileName, EventType[] eventTypes) {
        try (InputStream inStream = new FileInputStream(new File(inFolder + "/" + fileName));
             OutputStream outStream = new FileOutputStream(new File(outFolder + "/" + fileName))) {
            return parse(inStream, outStream, eventTypes, null);
        } catch (IOException ex) {
            return false;
        }
    }

    static public Boolean parse(InputStream inFile, OutputStream outFile, EventType[] eventTypes, Date sameDate) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inFile));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outFile))) {

            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String st;
            while ((st = br.readLine()) != null) {
                List<ParsedEvent> events = LineParser.parse(st, eventTypes, sameDate);

                for (ParsedEvent event : events) {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("eventTypeId", event.eventTypeId);
                    node.put("timestamp", df.format(event.eventTimestamp));
                    ObjectNode data = mapper.createObjectNode();
                    for (Map.Entry<String, String> entry : event.data.entrySet()) {
                        data.put(entry.getKey(), entry.getValue());
                    }
                    node.set("data", data);
                    String jsonString = node.toString();
                    bw.write(jsonString);
                    bw.newLine();
                }
            }
            bw.flush();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
