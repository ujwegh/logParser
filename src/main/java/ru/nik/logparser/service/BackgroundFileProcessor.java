package ru.nik.logparser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.nik.logparser.util.FileParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис обработки файлов с логами
 */
@Slf4j
@Service
public class BackgroundFileProcessor {
    @Value("${in_folder}")
    private String inFolder;

    @Value("${out_folder}")
    private String outFolder;

    private static List<String> processedFiles = new ArrayList<>();

    @SuppressWarnings("ConstantConditions")
    @Scheduled(fixedDelay = 1000)
    public void ProcessFiles() {
        File in = new File(inFolder);
        int count = 0;
        long start = System.currentTimeMillis();
        for (File file : in.listFiles()) {
            if (processedFiles.contains(file.getName())) continue;
            if (FileParser.parse(inFolder, outFolder, file.getName(), EventService.listEventTypes())) {
                processedFiles.add(file.getName());
                count++;
            }
        }
        long end = System.currentTimeMillis();
        long msec = end - start;

        if (count > 0) log.info("Processed {} files in {} msec", count, msec);
    }
}
