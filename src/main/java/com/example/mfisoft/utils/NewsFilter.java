package com.example.mfisoft.utils;

import com.example.mfisoft.models.Recordings;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.Set;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class NewsFilter {

    public boolean isValid(Recordings recordings, Set<String> blackList) {
        for (String s : blackList) {
            if (!recordings.title().toLowerCase().contains(s.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Set<String> createBlackList() throws URISyntaxException {
        Set<String> blackList = new CopyOnWriteArraySet<>();
        File file = new File(getClass().getResource("/blacklist.txt").toURI());
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                blackList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blackList;
    }
}
