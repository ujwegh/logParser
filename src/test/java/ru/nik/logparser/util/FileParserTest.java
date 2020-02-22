package ru.nik.logparser.util;

import org.junit.jupiter.api.Test;
import ru.nik.logparser.model.EventType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    @Test
    public void simpleFileTest() throws UnsupportedEncodingException {
        Map<String, String> params1 = new HashMap<>();
        params1.put("x1", "x1=(\\S*)");
        params1.put("x2", "x2\\s*=\\s*\\'([^\\']*)\\'");
        Map<String, String> params2 = new HashMap<>();
        params2.put("x1", "x1=(\\S*)");
        EventType[] eventTypes = new EventType[]{
                new EventType(1, "rule12", params1),
                new EventType(2, "rule12", params2),
                new EventType(3, "rule3", params2)};
        String testString = "rule12 x1=testx1 x2='test x2'\n rule3 x1=testx1";

        InputStream ifFile = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outFile = new ByteArrayOutputStream();
        Boolean parsed = FileParser.parse(ifFile, outFile, eventTypes, new Date(119, 11, 18));
        assertTrue(parsed);

        String result = outFile.toString(StandardCharsets.UTF_8.name());
        String expected = "{\"eventTypeId\":1,\"timestamp\":\"18.12.2019 00:00:00\",\"data\":{\"x1\":\"testx1\",\"x2\":\"test x2\"}}\r\n" +
                "{\"eventTypeId\":2,\"timestamp\":\"18.12.2019 00:00:00\",\"data\":{\"x1\":\"testx1\"}}\r\n" +
                "{\"eventTypeId\":3,\"timestamp\":\"18.12.2019 00:00:00\",\"data\":{\"x1\":\"testx1\"}}\r\n";
        assertEquals(expected, result);
    }

    @Test
    public void badInFileTest() {
        EventType[] eventTypes = new EventType[0];
        Boolean parsed = FileParser.parse("szdfsedrf", "out_folder", "access_log", eventTypes);
        assertFalse(parsed);
    }

    @Test
    public void badOutFileTest() {
        EventType[] eventTypes = new EventType[0];
        Boolean parsed = FileParser.parse("in_folder", "isrdhfni", "access_log", eventTypes);
        assertFalse(parsed);
    }
}