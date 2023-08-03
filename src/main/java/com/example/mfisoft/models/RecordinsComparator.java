package com.example.mfisoft.models;

import java.util.Comparator;

public class RecordinsComparator implements Comparator<Recordings> {
    @Override
    public int compare(Recordings r1, Recordings r2) {
        return r1.publishedAt().getNano() - r2.publishedAt().getNano();
    }
}
