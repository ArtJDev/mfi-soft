package com.example.mfisoft.downloader;

import com.example.mfisoft.entities.Article;
import com.example.mfisoft.models.Recordings;
import com.example.mfisoft.proxies.Params;
import com.example.mfisoft.proxies.RecordingsRequester;
import com.example.mfisoft.repository.ArticlesRepository;
import com.example.mfisoft.utils.NewsFilter;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;

@Component
public class RecordingsArticlesDownloader {
    @Value("${pool.size}")
    private int POOL_SIZE;
    @Value("${download.totalLimit}")
    private Integer totalLimit;
    @Value("${download.bufferNewsLimit}")
    private Integer bufferNewsLimit;
    @Value("${download.limit}")
    private Integer limit;
    private final AtomicInteger start = new AtomicInteger(0);
    private final AtomicInteger count = new AtomicInteger(0);
    private final Map<String, List<Recordings>> recordingsBuffer = new ConcurrentHashMap<>();
    private final NewsFilter newsFilter;
    private final RecordingsRequester recordingsRequester;
    private final ArticlesRepository articlesRepository;

    public RecordingsArticlesDownloader(NewsFilter newsFilter, RecordingsRequester recordingsRequester,
                                        ArticlesRepository articlesRepository) {
        this.newsFilter = newsFilter;
        this.recordingsRequester = recordingsRequester;
        this.articlesRepository = articlesRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws Exception {
        ForkJoinPool pool = new ForkJoinPool(POOL_SIZE);
        Set<String> blackList = newsFilter.createBlackList();

        CompletableFuture.runAsync(() -> {
            while (count.intValue() <= totalLimit) {
                List<Recordings> recordings = recordingsRequester.getRecords(new Params(limit, start.getAndAdd(limit)));
                count.getAndAdd(limit);
                Map<String, List<Recordings>> updatedRecordings = recordings.parallelStream()
                        .filter(r -> newsFilter.isValid(r, blackList))
                        .sorted(Comparator.comparing(Recordings::publishedAt))
                        .collect(groupingBy(Recordings::newsSite));
                if (!recordingsBuffer.isEmpty()) {
                    for (Map.Entry<String, List<Recordings>> bufferEntry : recordingsBuffer.entrySet()) {
                        if (bufferEntry.getValue().size() >= bufferNewsLimit) {
                            bufferEntry.getValue().forEach(this::saveArticle);
                            recordingsBuffer.remove(bufferEntry.getKey());
                        }
                    }
                }
                recordingsBuffer.putAll(recordingsToBuffer(updatedRecordings));
            }
            recordingsBuffer.forEach((key, value) -> value.forEach(this::saveArticle));
            recordingsBuffer.clear();
        }, pool);
    }

    private void saveArticle(Recordings recordings) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(recordings.url());
            request.setHeader(HttpHeaders.ACCEPT, ContentType.TEXT_HTML.getMimeType());
            CloseableHttpResponse response = httpClient.execute(request);
            byte[] article = response.getEntity().getContent().readAllBytes();
            articlesRepository.save(new Article(null,
                    recordings.title(),
                    recordings.newsSite(),
                    recordings.publishedAt(),
                    article));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<Recordings>> recordingsToBuffer(Map<String, List<Recordings>> updatedRecordings) {
        List<Recordings> agregateList = new ArrayList<>();
        recordingsBuffer.forEach((key, value) -> agregateList.addAll(value));
        updatedRecordings.forEach((key, value) -> agregateList.addAll(value));
        return agregateList.stream().sorted(Comparator.comparing(Recordings::publishedAt))
                .collect(groupingBy(Recordings::newsSite));
    }
}
